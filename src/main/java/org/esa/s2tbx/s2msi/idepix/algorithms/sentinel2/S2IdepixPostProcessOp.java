package org.esa.s2tbx.s2msi.idepix.algorithms.sentinel2;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.s2tbx.s2msi.idepix.operators.cloudshadow.S2IdepixCloudShadowOp;
import org.esa.s2tbx.s2msi.idepix.util.S2IdepixConstants;
import org.esa.s2tbx.s2msi.idepix.util.S2IdepixUtils;
import org.esa.snap.core.datamodel.Band;
import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.gpf.GPF;
import org.esa.snap.core.gpf.Operator;
import org.esa.snap.core.gpf.OperatorException;
import org.esa.snap.core.gpf.OperatorSpi;
import org.esa.snap.core.gpf.Tile;
import org.esa.snap.core.gpf.annotations.OperatorMetadata;
import org.esa.snap.core.gpf.annotations.Parameter;
import org.esa.snap.core.gpf.annotations.SourceProduct;
import org.esa.snap.core.util.ProductUtils;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * Operator used to consolidate cloud flag for Sentinel-2:
 * - coastline refinement? (tbd)
 * - cloud shadow (tbd)
 *
 * @author olafd
 */
@OperatorMetadata(alias = "Idepix.Sentinel2.Postprocess",
        version = "2.2",
        internal = true,
        authors = "Olaf Danne",
        copyright = "(c) 2016 by Brockmann Consult",
        description = "Refines the Sentinel-2 MSI pixel classification.")
public class S2IdepixPostProcessOp extends Operator {

    @SourceProduct(alias = "l1c")
    private Product l1cProduct;

    @SourceProduct(alias = "s2Classif")
    private Product s2ClassifProduct;

    @SourceProduct(alias = "s2CloudBuffer", optional = true)
    private Product s2CloudBufferProduct;      // has only classifFlagBand with buffer added

    @Parameter(defaultValue = "true", label = " Compute cloud shadow", description = " Compute cloud shadow")
    private boolean computeCloudShadow;

    @Parameter(description = "The mode by which clouds are detected. There are three options: Land/Water, Multiple Bands" +
            "or Single Band", valueSet = {"LandWater", "MultiBand", "SingleBand"}, defaultValue = "LandWater")
    private String mode;

    @Parameter(description = "Whether to also compute mountain shadow", defaultValue = "true")
    private boolean computeMountainShadow;

//    @Parameter(defaultValue = "2", label = "Width of cloud buffer (# of pixels)")
//    private int cloudBufferWidth;

    private Band s2ClassifFlagBand;
    private Band cloudBufferFlagBand;
    private Band cloudShadowFlagBand;

    @Override
    public void initialize() throws OperatorException {

        Product postProcessedCloudProduct = createTargetProduct(s2ClassifProduct.getName(),
                                                                s2ClassifProduct.getProductType());

        s2ClassifFlagBand = s2ClassifProduct.getBand(S2IdepixUtils.IDEPIX_CLASSIF_FLAGS);
        if (s2CloudBufferProduct != null) {
            cloudBufferFlagBand = s2CloudBufferProduct.getBand(S2IdepixUtils.IDEPIX_CLASSIF_FLAGS);
        }

        cloudShadowFlagBand = null;
        if (computeCloudShadow) {
            HashMap<String, Product> input = new HashMap<>();
            input.put("s2ClassifProduct", s2ClassifProduct);
            input.put("s2CloudBufferProduct", s2CloudBufferProduct);
            Map<String, Object> params = new HashMap<>();
            params.put("computeCloudShadow", computeCloudShadow);
            params.put("computeMountainShadow", computeMountainShadow);
            params.put("mode", mode);
            final Product cloudShadowProduct = GPF.createProduct(OperatorSpi.getOperatorAlias(S2IdepixCloudShadowOp.class),
                                                                 params, input);
            cloudShadowFlagBand = cloudShadowProduct.getBand(S2IdepixCloudShadowOp.BAND_NAME_CLOUD_SHADOW);
        }

        ProductUtils.copyBand(S2IdepixUtils.IDEPIX_CLASSIF_FLAGS, s2ClassifProduct, postProcessedCloudProduct, false);
        setTargetProduct(postProcessedCloudProduct);
    }

    private Product createTargetProduct(String name, String type) {
        final int sceneWidth = s2ClassifProduct.getSceneRasterWidth();
        final int sceneHeight = s2ClassifProduct.getSceneRasterHeight();

        Product targetProduct = new Product(name, type, sceneWidth, sceneHeight);
        ProductUtils.copyGeoCoding(s2ClassifProduct, targetProduct);
        targetProduct.setStartTime(s2ClassifProduct.getStartTime());
        targetProduct.setEndTime(s2ClassifProduct.getEndTime());

        return targetProduct;
    }

    @Override
    public void computeTile(Band targetBand, final Tile targetTile, ProgressMonitor pm) throws OperatorException {
        Rectangle targetRectangle = targetTile.getRectangle();

        final Tile classifFlagTile = getSourceTile(s2ClassifFlagBand, targetRectangle);
        Tile cloudBufferFlagTile = null;
        if (s2CloudBufferProduct != null) {
            cloudBufferFlagTile = getSourceTile(cloudBufferFlagBand, targetRectangle);
        }

        for (int y = targetRectangle.y; y < targetRectangle.y + targetRectangle.height; y++) {
            checkForCancellation();
            for (int x = targetRectangle.x; x < targetRectangle.x + targetRectangle.width; x++) {

                if (targetRectangle.contains(x, y)) {
                    boolean isInvalid = targetTile.getSampleBit(x, y, S2IdepixConstants.IDEPIX_INVALID);
                    if (!isInvalid) {
                        combineFlags(x, y, classifFlagTile, targetTile);
                        if (s2CloudBufferProduct != null) {
                            combineFlags(x, y, cloudBufferFlagTile, targetTile);
                        }
                    }
                }
            }
        }

        if (computeCloudShadow) {
            final Tile flagTile = getSourceTile(cloudShadowFlagBand, targetRectangle);
            int cloudShadowFlag = (int) Math.pow(2, S2IdepixCloudShadowOp.F_CLOUD_SHADOW);
            int mountainShadowFlag = (int) Math.pow(2, S2IdepixCloudShadowOp.F_MOUNTAIN_SHADOW);
            int cloudBufferFlag = (int) Math.pow(2, S2IdepixCloudShadowOp.F_CLOUD_BUFFER);
            for (int y = targetRectangle.y; y < targetRectangle.y + targetRectangle.height; y++) {
                checkForCancellation();
                for (int x = targetRectangle.x; x < targetRectangle.x + targetRectangle.width; x++) {
                    final int flagValue = flagTile.getSampleInt(x, y);
                    if ((flagValue & cloudShadowFlag) == cloudShadowFlag) {
                        targetTile.setSample(x, y, S2IdepixConstants.IDEPIX_CLOUD_SHADOW, true);
                    } else if ((flagValue & cloudBufferFlag) == cloudBufferFlag) {
                        targetTile.setSample(x, y, S2IdepixConstants.IDEPIX_CLOUD_BUFFER, true);
                    }
                    if (computeMountainShadow && (flagValue & mountainShadowFlag) == mountainShadowFlag) {
                        targetTile.setSample(x, y, S2IdepixConstants.IDEPIX_MOUNTAIN_SHADOW, true);
                    }
                }
            }
        }
    }

    private void combineFlags(int x, int y, Tile sourceFlagTile, Tile targetTile) {
        int sourceFlags = sourceFlagTile.getSampleInt(x, y);
        int computedFlags = targetTile.getSampleInt(x, y);
        targetTile.setSample(x, y, sourceFlags | computedFlags);
    }

    /**
     * The Service Provider Interface (SPI) for the operator.
     * It provides operator meta-data and is a factory for new operator instances.
     */
    public static class Spi extends OperatorSpi {

        public Spi() {
            super(S2IdepixPostProcessOp.class);
        }
    }
}
