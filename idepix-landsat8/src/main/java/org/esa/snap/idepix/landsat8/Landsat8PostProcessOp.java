package org.esa.snap.idepix.landsat8;

import com.bc.ceres.core.ProgressMonitor;
import org.esa.snap.core.datamodel.*;
import org.esa.snap.core.gpf.Operator;
import org.esa.snap.core.gpf.OperatorException;
import org.esa.snap.core.gpf.OperatorSpi;
import org.esa.snap.core.gpf.Tile;
import org.esa.snap.core.gpf.annotations.OperatorMetadata;
import org.esa.snap.core.gpf.annotations.Parameter;
import org.esa.snap.core.gpf.annotations.SourceProduct;
import org.esa.snap.core.util.ProductUtils;
import org.esa.snap.core.util.RectangleExtender;
import org.esa.snap.idepix.core.CloudShadowFronts;
import org.esa.snap.idepix.core.IdepixConstants;
import org.esa.snap.idepix.core.operators.CloudBuffer;
import org.esa.snap.idepix.core.util.IdepixUtils;
import org.esa.snap.idepix.core.util.OperatorUtils;

import java.awt.*;

/**
 * Operator used to consolidate cloud flag for Landsat 8:
 * - coastline refinement
 * - cloud buffer (LC algo as default)
 * - cloud shadow (tbd)
 *
 * @author olafd
 */
@OperatorMetadata(alias = "Idepix.L8.Postprocess",
        version = "3.0",
        internal = true,
        authors = "Marco Peters, Marco Zuehlke, Olaf Danne",
        copyright = "(c) 2016 by Brockmann Consult",
        description = "Refines the Landsat-8 pixel classification.")
public class Landsat8PostProcessOp extends Operator {

    @Parameter(defaultValue = "2", label = "Width of cloud buffer (# of pixels)")
    private int cloudBufferWidth;

    @Parameter(defaultValue = "false", label = " Compute a cloud buffer")
    private boolean computeCloudBuffer;

    //    @Parameter(defaultValue = "true",
//            label = " Compute cloud shadow",
//            description = " Compute cloud shadow with latest 'fronts' algorithm")
    private boolean computeCloudShadow = false;   // todo: we have no info at all for this (pressure, height, temperature)

    @Parameter(defaultValue = "false",
            label = " Refine pixel classification near coastlines (time consuming!)",
            description = "Refine pixel classification near coastlines. ")
    private boolean refineClassificationNearCoastlines;

    @Parameter(label = " Cloud classification band suffix")
    private String cloudClassifBandSuffix;

    @SourceProduct(alias = "landsatCloud")
    private Product landsatCloudProduct;
    @SourceProduct(alias = "waterMask", optional = true)
    private Product waterMaskProduct;

    private Band waterFractionBand;
    private Band origCloudFlagBand;
    private GeoCoding geoCoding;

    private RectangleExtender rectCalculator;

    @Override
    public void initialize() throws OperatorException {

        if (!computeCloudBuffer && !computeCloudShadow && !refineClassificationNearCoastlines) {
            setTargetProduct(landsatCloudProduct);
        } else {
            Product postProcessedCloudProduct = OperatorUtils.createCompatibleProduct(landsatCloudProduct,
                                                                                      "postProcessedCloud", "postProcessedCloud");

            if (waterMaskProduct != null) {
                waterFractionBand = waterMaskProduct.getBand("land_water_fraction");
            }

            geoCoding = landsatCloudProduct.getSceneGeoCoding();

            final String cloudClassifBandName = IdepixConstants.CLASSIF_BAND_NAME;
            origCloudFlagBand = landsatCloudProduct.getBand(cloudClassifBandName);
            int extendedWidth = 64;
            int extendedHeight = 64; // todo: what do we need?

            rectCalculator = new RectangleExtender(new Rectangle(landsatCloudProduct.getSceneRasterWidth(),
                                                                 landsatCloudProduct.getSceneRasterHeight()),
                                                   extendedWidth, extendedHeight
            );


            ProductUtils.copyBand(cloudClassifBandName, landsatCloudProduct, postProcessedCloudProduct, false);
            setTargetProduct(postProcessedCloudProduct);
        }
    }

    @Override
    public void computeTile(Band targetBand, final Tile targetTile, ProgressMonitor pm) throws OperatorException {
        Rectangle targetRectangle = targetTile.getRectangle();
        final Rectangle srcRectangle = rectCalculator.extend(targetRectangle);

        final Tile sourceFlagTile = getSourceTile(origCloudFlagBand, srcRectangle);
        Tile waterFractionTile = null;
        if (waterFractionBand != null) {
            waterFractionTile = getSourceTile(waterFractionBand, srcRectangle);
        }

        for (int y = srcRectangle.y; y < srcRectangle.y + srcRectangle.height; y++) {
            checkForCancellation();
            for (int x = srcRectangle.x; x < srcRectangle.x + srcRectangle.width; x++) {

                if (targetRectangle.contains(x, y)) {
                    combineFlags(x, y, sourceFlagTile, targetTile);

                    postProcess(x, y, targetTile, srcRectangle, sourceFlagTile, waterFractionTile,
                                Landsat8Constants.IDEPIX_CLOUD_SHIMEZ);
                    postProcess(x, y, targetTile, srcRectangle, sourceFlagTile, waterFractionTile,
                                Landsat8Constants.IDEPIX_CLOUD_HOT);
                    postProcess(x, y, targetTile, srcRectangle, sourceFlagTile, waterFractionTile,
                                Landsat8Constants.IDEPIX_CLOUD_OTSU);
                    postProcess(x, y, targetTile, srcRectangle, sourceFlagTile, waterFractionTile,
                                Landsat8Constants.IDEPIX_CLOUD_CLOST);

                    postProcess(x, y, targetTile, srcRectangle, sourceFlagTile, waterFractionTile,
                                IdepixConstants.IDEPIX_CLOUD_SURE);
                    targetTile.setSample(x, y, IdepixConstants.IDEPIX_CLOUD,
                                         targetTile.getSampleBit(x, y, IdepixConstants.IDEPIX_CLOUD_SURE));
                }
            }
        }

        if (computeCloudBuffer) {
            for (int y = srcRectangle.y; y < srcRectangle.y + srcRectangle.height; y++) {
                for (int x = srcRectangle.x; x < srcRectangle.x + srcRectangle.width; x++) {
                    // SHIMEZ
                    final boolean isCloudShimez = sourceFlagTile.getSampleBit(x, y, Landsat8Constants.IDEPIX_CLOUD_SHIMEZ);
                    if (isCloudShimez) {
                        CloudBuffer.computeSimpleCloudBuffer(x, y,
                                                             targetTile,
                                                             srcRectangle,
                                                             cloudBufferWidth,
                                                             Landsat8Constants.IDEPIX_CLOUD_SHIMEZ_BUFFER);
                    }
                    // HOT
                    final boolean isCloudHot = sourceFlagTile.getSampleBit(x, y, Landsat8Constants.IDEPIX_CLOUD_HOT);
                    if (isCloudHot) {
                        CloudBuffer.computeSimpleCloudBuffer(x, y,
                                                             targetTile,
                                                             srcRectangle,
                                                             cloudBufferWidth,
                                                             Landsat8Constants.IDEPIX_CLOUD_HOT_BUFFER);
                    }
                    // OTSU
                    final boolean isCloudOtsu = sourceFlagTile.getSampleBit(x, y, Landsat8Constants.IDEPIX_CLOUD_OTSU);
                    if (isCloudOtsu) {
                        CloudBuffer.computeSimpleCloudBuffer(x, y,
                                                             targetTile,
                                                             srcRectangle,
                                                             cloudBufferWidth,
                                                             Landsat8Constants.IDEPIX_CLOUD_OTSU_BUFFER);
                    }
                    // CLOST
                    final boolean isCloudClost = sourceFlagTile.getSampleBit(x, y, Landsat8Constants.IDEPIX_CLOUD_CLOST);
                    if (isCloudClost) {
                        CloudBuffer.computeSimpleCloudBuffer(x, y,
                                                             targetTile,
                                                             srcRectangle,
                                                             cloudBufferWidth,
                                                             Landsat8Constants.IDEPIX_CLOUD_CLOST_BUFFER);
                    }
                    // overall
                    final boolean isCloud = sourceFlagTile.getSampleBit(x, y, IdepixConstants.IDEPIX_CLOUD);
                    if (isCloud) {
                        CloudBuffer.computeSimpleCloudBuffer(x, y,
                                                             targetTile,
                                                             srcRectangle,
                                                             cloudBufferWidth,
                                                             IdepixConstants.IDEPIX_CLOUD_BUFFER);
                    }
                }
            }

            for (int y = targetRectangle.y; y < targetRectangle.y + targetRectangle.height; y++) {
                checkForCancellation();
                for (int x = targetRectangle.x; x < targetRectangle.x + targetRectangle.width; x++) {
                    consolidateLandsatCloudsAndBuffers(targetTile, x, y,
                                                       Landsat8Constants.IDEPIX_CLOUD_SHIMEZ,
                                                       Landsat8Constants.IDEPIX_CLOUD_SHIMEZ_BUFFER);
                    consolidateLandsatCloudsAndBuffers(targetTile, x, y,
                                                       Landsat8Constants.IDEPIX_CLOUD_HOT,
                                                       Landsat8Constants.IDEPIX_CLOUD_HOT_BUFFER);
                    consolidateLandsatCloudsAndBuffers(targetTile, x, y,
                                                       Landsat8Constants.IDEPIX_CLOUD_OTSU,
                                                       Landsat8Constants.IDEPIX_CLOUD_OTSU_BUFFER);
                    consolidateLandsatCloudsAndBuffers(targetTile, x, y,
                                                       Landsat8Constants.IDEPIX_CLOUD_CLOST,
                                                       Landsat8Constants.IDEPIX_CLOUD_CLOST_BUFFER);
                    IdepixUtils.consolidateCloudAndBuffer(targetTile, x, y);
                }
            }
        }

        if (computeCloudShadow) {
            // todo: algorithm needed
        }
    }

    private static void consolidateLandsatCloudsAndBuffers(Tile targetTile, int x, int y,
                                                           int cloudFlagBit, int cloudBufferFlagBit) {
        if (targetTile.getSampleBit(x, y, cloudFlagBit)) {
            targetTile.setSample(x, y, cloudBufferFlagBit, false);
        }
    }

    private void postProcess(int x, int y, Tile targetTile, Rectangle srcRectangle, Tile sourceFlagTile, Tile waterFractionTile,
                             int cloudFlagBit) {
        boolean isCloud = sourceFlagTile.getSampleBit(x, y, cloudFlagBit);
        if (refineClassificationNearCoastlines && waterFractionTile != null) {
            if (isNearCoastline(x, y, waterFractionTile, srcRectangle)) {
                targetTile.setSample(x, y, IdepixConstants.IDEPIX_COASTLINE, true);
                refineSnowIceFlaggingForCoastlines(x, y, sourceFlagTile, targetTile);
                if (isCloud) {
                    refineCloudFlaggingForCoastlines(x, y, cloudFlagBit,
                                                     sourceFlagTile, waterFractionTile, targetTile, srcRectangle);
                }
            }
        }
        boolean isCloudAfterRefinement = targetTile.getSampleBit(x, y, cloudFlagBit);
        if (isCloudAfterRefinement) {
            targetTile.setSample(x, y, IdepixConstants.IDEPIX_SNOW_ICE, false);
        }
    }

    private void combineFlags(int x, int y, Tile sourceFlagTile, Tile targetTile) {
        int sourceFlags = sourceFlagTile.getSampleInt(x, y);
        int computedFlags = targetTile.getSampleInt(x, y);
        targetTile.setSample(x, y, sourceFlags | computedFlags);
    }

    private boolean isCoastlinePixel(int x, int y, Tile waterFractionTile) {
        boolean isCoastline = false;
        // the water mask ends at 59 Degree south, stop earlier to avoid artefacts
        if (IdepixUtils.getGeoPos(geoCoding, x, y).lat > -58f) {
            final int waterFraction = waterFractionTile.getSampleInt(x, y);
            // values bigger than 100 indicate no data
            if (waterFraction <= 100) {
                // todo: this does not work if we have a PixelGeocoding. In that case, waterFraction
                // is always 0 or 100!! (TS, OD, 20140502)
                isCoastline = waterFraction < 100 && waterFraction > 0;
            }
        }
        return isCoastline;
    }

    private boolean isNearCoastline(int x, int y, Tile waterFractionTile, Rectangle rectangle) {
        final int windowWidth = 1;
        final int LEFT_BORDER = Math.max(x - windowWidth, rectangle.x);
        final int RIGHT_BORDER = Math.min(x + windowWidth, rectangle.x + rectangle.width - 1);
        final int TOP_BORDER = Math.max(y - windowWidth, rectangle.y);
        final int BOTTOM_BORDER = Math.min(y + windowWidth, rectangle.y + rectangle.height - 1);
        final int waterFractionCenter = waterFractionTile.getSampleInt(x, y);
        for (int i = LEFT_BORDER; i <= RIGHT_BORDER; i++) {
            for (int j = TOP_BORDER; j <= BOTTOM_BORDER; j++) {
                if (rectangle.contains(i, j)) {
                    if (!(landsatCloudProduct.getSceneGeoCoding() instanceof TiePointGeoCoding) &&
                            !(landsatCloudProduct.getSceneGeoCoding() instanceof CrsGeoCoding)) {
                        if (waterFractionTile.getSampleInt(i, j) != waterFractionCenter) {
                            return true;
                        }
                    } else {
                        if (isCoastlinePixel(i, j, waterFractionTile)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void refineCloudFlaggingForCoastlines(int x, int y, int cloudFlagBit,
                                                  Tile sourceFlagTile, Tile waterFractionTile, Tile targetTile,
                                                  Rectangle srcRectangle) {
        final int windowWidth = 1;
        final int LEFT_BORDER = Math.max(x - windowWidth, srcRectangle.x);
        final int RIGHT_BORDER = Math.min(x + windowWidth, srcRectangle.x + srcRectangle.width - 1);
        final int TOP_BORDER = Math.max(y - windowWidth, srcRectangle.y);
        final int BOTTOM_BORDER = Math.min(y + windowWidth, srcRectangle.y + srcRectangle.height - 1);
        boolean removeCloudFlag = true;
        if (CloudShadowFronts.isPixelSurrounded(x, y, sourceFlagTile, cloudFlagBit)) {
            removeCloudFlag = false;
        } else {
            Rectangle targetTileRectangle = targetTile.getRectangle();
            for (int i = LEFT_BORDER; i <= RIGHT_BORDER; i++) {
                for (int j = TOP_BORDER; j <= BOTTOM_BORDER; j++) {
                    boolean is_cloud = sourceFlagTile.getSampleBit(i, j, cloudFlagBit);
                    if (is_cloud && targetTileRectangle.contains(i, j) && !isNearCoastline(i, j, waterFractionTile, srcRectangle)) {
                        removeCloudFlag = false;
                        break;
                    }
                }
            }
        }

        if (removeCloudFlag) {
            targetTile.setSample(x, y, Landsat8Constants.IDEPIX_CLOUD_SHIMEZ, false);
            targetTile.setSample(x, y, Landsat8Constants.IDEPIX_CLOUD_CLOST, false);
            targetTile.setSample(x, y, Landsat8Constants.IDEPIX_CLOUD_HOT, false);
            targetTile.setSample(x, y, Landsat8Constants.IDEPIX_CLOUD_OTSU, false);
            targetTile.setSample(x, y, IdepixConstants.IDEPIX_CLOUD, false);
            targetTile.setSample(x, y, IdepixConstants.IDEPIX_CLOUD_SURE, false);
            targetTile.setSample(x, y, IdepixConstants.IDEPIX_CLOUD_AMBIGUOUS, false);
        }
    }

    private void refineSnowIceFlaggingForCoastlines(int x, int y, Tile sourceFlagTile, Tile targetTile) {
        final boolean isSnowIce = sourceFlagTile.getSampleBit(x, y, IdepixConstants.IDEPIX_SNOW_ICE);
        if (isSnowIce) {
            targetTile.setSample(x, y, IdepixConstants.IDEPIX_SNOW_ICE, false);
        }
    }

    public static class Spi extends OperatorSpi {

        public Spi() {
            super(Landsat8PostProcessOp.class);
        }
    }
}
