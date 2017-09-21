package org.esa.s2tbx.s2msi.idepix.operators.cloudshadow;

import org.esa.snap.core.datamodel.Product;
import org.esa.snap.core.util.math.MathUtils;

import java.awt.Rectangle;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.util.Arrays;

/**
 * @author Grit Kirches
 * @author Michael Paperin
 */
class MountainShadowAreasPathCentralPixel {

    private static final boolean SHADOW_ADAPTER_SZA = true;
    private static final double MAX_MOUNTAIN_TOP = 8850;
    private static final int MEAN_EARTH_RADIUS = 6372000;
    private static final int MOUNTAIN_SHADOW = 1;
    private static final int MOUNTAIN_NO_SHADOW = 0;

    static int[] makeMountainShadowArea(Product sourceProduct, Product targetProduct, Rectangle sourceRectangle,
                                               Rectangle targetRectangle, float[] sourceSunZenith, float[] sourceSunAzimuth,
                                               float[] sourceLatitude, float[] sourceLongitude, float[] sourceAltitude,
                                               int[] flagArray) {
        int X0;
        int Y0;

        int X0TargetCenter;
        int Y0TargetCenter;
        int X0SourceCenter;
        int Y0SourceCenter;

        int X0Center;
        int Y0Center;
        int X1Center;
        int Y1Center;

        int productHeight = sourceProduct.getSceneRasterHeight();
        int productWidth = sourceProduct.getSceneRasterWidth();
        int targetHeight = targetRectangle.height;
        int targetWidth = targetRectangle.width;
        int sourceHeight = sourceRectangle.height;
        int sourceWidth = sourceRectangle.width;
        int xSourceLocation = sourceRectangle.x;
        int ySourceLocation = sourceRectangle.y;

        final int[] mountainShadowArray = new int[sourceWidth * sourceHeight];

        boolean caseKeyBorder = !(xSourceLocation >= 0 && ySourceLocation >= 0 &&
                xSourceLocation + sourceWidth < productWidth && ySourceLocation + sourceHeight < productHeight);

        int[] path;
        int pathWidth;
        int pathHeight;
        int pathRectangleWidth;
        int pathRectangleHeight;

        int x0TargetProductEquivalent;
        int y0TargetProductEquivalent;
        int x1TargetProductEquivalent;
        int y1TargetProductEquivalent;

        double sunZenithIntermediate;
        double sunZenithDegree;
        double sunZenithRad;
        double sunAzimuthRad;
        double surfaceAltitude;

        double[] pathArray;

        int[] linePointsArray = new int[4];

        int[] indicesPointPathArray = new int[4];
        int[][] indicesPathArray = new int[1][1];
        indicesPathArray[0][0] = 0;
        int x0 = 0;
        int y0 = 0;
        int counter = 0;

        X0TargetCenter = (int) (targetRectangle.width / 2.);
        Y0TargetCenter = (int) (targetRectangle.width / 2.);
        X0SourceCenter = X0TargetCenter + S2IdepixCloudShadowOp.searchBorderRadius;
        Y0SourceCenter = Y0TargetCenter + S2IdepixCloudShadowOp.searchBorderRadius;

        sunZenithDegree = sourceSunZenith[Y0SourceCenter * sourceRectangle.width + X0SourceCenter];

        if (SHADOW_ADAPTER_SZA) {
            sunZenithIntermediate = sunZenithDegree * (2. * Math.pow(((90. - sunZenithDegree) / 90), 3) + 1.);
        } else {
            sunZenithIntermediate = sunZenithDegree;
        }

        if (sunZenithIntermediate > 89.0) {
            sunZenithIntermediate = 89.0;
        }

        System.out.printf("sun zenith angle: %f adapted sun zenith angle: %f \n", sunZenithDegree, sunZenithIntermediate);
        sunZenithRad = sunZenithIntermediate * MathUtils.DTOR;

        sunAzimuthRad = sourceSunAzimuth[Y0SourceCenter * sourceRectangle.width + X0SourceCenter] * MathUtils.DTOR;
        surfaceAltitude = sourceAltitude[Y0SourceCenter * sourceRectangle.width + X0SourceCenter];

        path = getPotentialMaxMountainShadowPosition(surfaceAltitude, sunZenithRad, sunAzimuthRad,
                                                     MAX_MOUNTAIN_TOP, X0SourceCenter, Y0SourceCenter, X0TargetCenter, Y0TargetCenter,
                                                     productHeight, productWidth, sourceHeight, sourceWidth, targetHeight, targetWidth,
                                                     S2IdepixCloudShadowOp.spatialResolution, caseKeyBorder);

        X0Center = path[0];
        Y0Center = path[1];
        X1Center = path[2];
        Y1Center = path[3];

        pathWidth = X1Center - X0Center;
        pathHeight = Y1Center - Y0Center;

        if (pathWidth < 0) {
            pathRectangleWidth = pathWidth - 1;
        } else {
            pathRectangleWidth = pathWidth + 1;
        }
        if (pathHeight < 0) {
            pathRectangleHeight = pathHeight - 1;
        } else {
            pathRectangleHeight = pathHeight + 1;
        }


        Arrays.fill(linePointsArray, 0);
        getLinePoints(X0Center, Y0Center, X1Center, Y1Center, sourceHeight, sourceWidth, targetHeight, targetWidth, pathWidth,
                      pathHeight, linePointsArray, caseKeyBorder);

        x0TargetProductEquivalent = linePointsArray[0];
        y0TargetProductEquivalent = linePointsArray[1];
        x1TargetProductEquivalent = linePointsArray[2];
        y1TargetProductEquivalent = linePointsArray[3];

        int absPathRectangleWidth = Math.abs(pathRectangleWidth);
        int absPathRectangleHeight = Math.abs(pathRectangleHeight);
        int absPathRectangleLength = absPathRectangleHeight * absPathRectangleWidth;

        if (pathHeight != 0 && pathWidth != 0) {
            RenderedImage lineImage = PathRasterizer.rasterizeLine(x0TargetProductEquivalent, y0TargetProductEquivalent,
                                                                   x1TargetProductEquivalent, y1TargetProductEquivalent, targetProduct);
            Rectangle pathAbsRectangle = new Rectangle(Math.min(x0TargetProductEquivalent, x1TargetProductEquivalent),
                                                       Math.min(y0TargetProductEquivalent, y1TargetProductEquivalent),
                                                       absPathRectangleWidth, absPathRectangleHeight);
            Raster pathRaster = lineImage.getData(pathAbsRectangle);
            pathArray = new double[absPathRectangleLength];
            pathRaster.getSamples(Math.min(x0TargetProductEquivalent, x1TargetProductEquivalent),
                                  Math.min(y0TargetProductEquivalent, y1TargetProductEquivalent),
                                  absPathRectangleWidth, absPathRectangleHeight, 0, pathArray);
            indicesPathArray = new int[absPathRectangleLength][2];
            counter = 0;
            for (int jj = 0; jj < absPathRectangleHeight; jj++) {
                for (int ii = 0; ii < absPathRectangleWidth; ii++) {
                    getIndicesPathArray(X0Center, Y0Center, X1Center, Y1Center, x0, y0, ii, jj,
                                        absPathRectangleWidth - 1, absPathRectangleHeight - 1, indicesPointPathArray);
                    if ((int) pathArray[indicesPointPathArray[3] * absPathRectangleWidth + indicesPointPathArray[2]] > 0) {
                        indicesPathArray[counter][0] = indicesPointPathArray[0]; // xPath
                        indicesPathArray[counter][1] = indicesPointPathArray[1]; // yPath
                        counter++;
                    }
                }
            }
        } else if (pathHeight == 0 && pathWidth != 0) {
            indicesPathArray = new int[absPathRectangleWidth][2];
            counter = 0;
            for (int jj = 0; jj < absPathRectangleHeight; jj++) {
                for (int ii = 0; ii < absPathRectangleWidth; ii++) {
                    getIndicesPathArray(X0Center, Y0Center, X1Center, Y1Center, x0, y0, ii, jj,
                                        absPathRectangleWidth - 1, absPathRectangleHeight - 1, indicesPointPathArray);
                    indicesPathArray[counter][0] = indicesPointPathArray[0]; // xPath
                    indicesPathArray[counter][1] = indicesPointPathArray[1]; // yPath
                    counter++;
                }
            }
        } else if (pathHeight != 0) {
            indicesPathArray = new int[absPathRectangleHeight][2];
            counter = 0;
            for (int jj = 0; jj < absPathRectangleHeight; jj++) {
                for (int ii = 0; ii < absPathRectangleWidth; ii++) {
                    getIndicesPathArray(X0Center, Y0Center, X1Center, Y1Center, x0, y0, ii, jj,
                                        absPathRectangleWidth - 1, absPathRectangleHeight - 1, indicesPointPathArray);
                    indicesPathArray[counter][0] = indicesPointPathArray[0]; // xPath
                    indicesPathArray[counter][1] = indicesPointPathArray[1]; // yPath
                    counter++;
                }
            }
        }

        for (int y = 0; y < sourceHeight; y++) {
            for (int x = 0; x < sourceWidth; x++) {

                X0 = x; // position in the source array
                Y0 = y;


                if (mountainShadowArray[Y0 * sourceRectangle.width + X0] < MOUNTAIN_SHADOW) {
                    identifyMountainShadow(sourceRectangle, sourceLatitude, sourceLongitude,
                                           sourceAltitude, flagArray, X0, Y0, sunZenithRad, indicesPathArray, counter, mountainShadowArray);
                }
            }
        }
        return mountainShadowArray;
    }


    private static void identifyMountainShadow(Rectangle sourceRectangle, float[] sourceLatitude,
                                               float[] sourceLongitude, float[] sourceAltitude,
                                               int[] flagArray, int x0, int y0, double sunZenith,
                                               int[][] indicesPathArray, int counter, int[] mountainShadowArray) {
        int index = y0 * sourceRectangle.width + x0;
        for (int n = 1; n < counter - 1; n++) {
            int xPath = x0 + indicesPathArray[n][0];
            int yPath = y0 + indicesPathArray[n][1];
            if (yPath < sourceRectangle.height && xPath < sourceRectangle.width && yPath >= 0 && xPath >= 0) {
                double[] mountainExtent = MountainVerticalExtent.
                        getMountainVerticalExtent(sourceRectangle, sourceAltitude, yPath, xPath);
                double mountainBase = mountainExtent[0];
                double mountainTop = mountainExtent[1];
                double tempAltitude = sourceAltitude[index];
                double x0y0Altitude;
                if (tempAltitude <= 0 || Double.isNaN(tempAltitude))
                    x0y0Altitude = Double.NaN; // [m]
                else {
                    x0y0Altitude = tempAltitude; // [m]
                }
                if (!Double.isNaN(mountainTop) && !Double.isNaN(x0y0Altitude)) {
                    int index0 = y0 * sourceRectangle.width + x0;
                    int indexPath = yPath * sourceRectangle.width + xPath;
                    double[] distAltArray = computeDistance(index0, indexPath, sourceLongitude, sourceLatitude,
                                                            sourceAltitude);
                    double minAltitude = distAltArray[1];
                    double mountainSearchPointHeight = distAltArray[0] * Math.tan(((Math.PI / 2. - sunZenith)));
                    mountainSearchPointHeight = mountainSearchPointHeight + (sourceAltitude[index] - minAltitude);
                    if (mountainBase <= mountainSearchPointHeight && mountainSearchPointHeight <= (mountainTop - minAltitude)) {
                        mountainShadowArray[index] = MOUNTAIN_SHADOW;
                        if (!((flagArray[index] & PreparationMaskBand.MOUNTAIN_SHADOW_FLAG) == PreparationMaskBand.MOUNTAIN_SHADOW_FLAG)) {
                            flagArray[index] += PreparationMaskBand.MOUNTAIN_SHADOW_FLAG;
                        }
                        return;
                    }
                }
            }
        }
    }

    private static double[] computeDistance(int index0, int indexPath, float[] sourceLongitude, float[] sourceLatitude,
                                            float[] sourceAltitude) {
        double k = Math.PI / 180.0;
        double geoPos1Lon = sourceLongitude[index0];
        double geoPos1Lat = sourceLatitude[index0];
        double geoPos2Lon = sourceLongitude[indexPath];
        double geoPos2Lat = sourceLatitude[indexPath];
        double minAltitude = (double) Math.min(sourceAltitude[index0], sourceAltitude[indexPath]);

        double cosPos1Lat = Math.cos(geoPos1Lat * k);
        double cosPos2Lat = Math.cos(geoPos2Lat * k);
        double sinPos1Lat = Math.sin(geoPos1Lat * k);
        double sinPos2Lat = Math.sin(geoPos2Lat * k);
        double delta = (geoPos2Lon - geoPos1Lon) * k;
        double cosDelta = Math.cos(delta);
        double sinDelta = Math.sin(delta);
        double y = Math.sqrt(Math.pow(cosPos2Lat * sinDelta, 2) +
                                     Math.pow(cosPos1Lat * sinPos2Lat - sinPos1Lat * cosPos2Lat * cosDelta, 2));
        double x = sinPos1Lat * sinPos2Lat + cosPos1Lat * cosPos2Lat * cosDelta;
        double ad = Math.atan2(y, x);
        double[] distAltArray = new double[3];
        distAltArray[0] = ad * (MEAN_EARTH_RADIUS + minAltitude);
        distAltArray[1] = minAltitude;
        return distAltArray;
    }

    private static int[] getPotentialMaxMountainShadowPosition(double surfaceAlt, double sza, double saa,
                                                               double maxAlt,
                                                               int x0SourceArray, int y0SourceArray,
                                                               int x0TargetArray, int y0TargetArray, int productHeight,
                                                               int productWidth, int sourceHeight, int sourceWidth,
                                                               int targetHeight, int targetWidth,
                                                               double spatialResolution, boolean caseKeyBorder) {


        int X0;
        int Y0;
        int X1;
        int Y1;
        double factor;
        int[] path = new int[4];
        double iteratedeltaProjX;
        double iteratedeltaProjY;

        double deltaProjX = ((maxAlt - surfaceAlt) * Math.tan(sza) * Math.cos(saa - Math.PI / 2.)) / spatialResolution;
        double deltaProjY = ((maxAlt - surfaceAlt) * Math.tan(sza) * Math.sin(saa - Math.PI / 2.)) / spatialResolution;


        int divisor = (int) (Math.max(Math.abs(deltaProjX), Math.abs(deltaProjX)) * 2 + 0.5);
        if (caseKeyBorder) {
            X0 = x0TargetArray;
            Y0 = y0TargetArray;
            X1 = X0 + (int) (deltaProjX + 0.5);
            Y1 = Y0 + (int) (deltaProjY + 0.5);

            if (X1 > productWidth || X1 > targetWidth || Y1 > productHeight || Y1 > targetHeight || X1 < 0 || Y1 < 0) {
                iteratedeltaProjX = deltaProjX / divisor;
                iteratedeltaProjY = deltaProjY / divisor;
                for (int kk = 0; kk < (int) (maxAlt) - 1; kk++) {
                    factor = divisor - kk;
                    X1 = X0 + (int) (factor * iteratedeltaProjX + 0.5);
                    Y1 = Y0 + (int) (factor * iteratedeltaProjY + 0.5);
                    if (X1 < productWidth && X1 < targetWidth && Y1 < productHeight && Y1 < targetHeight && X1 > 0 && Y1 > 0) {
                        break;
                    } else {
                        X1 = X0;
                        Y1 = Y0;
                    }
                }
            }
        } else {
            X0 = x0SourceArray;
            Y0 = y0SourceArray;
            X1 = X0 + (int) (deltaProjX + 0.5);
            Y1 = Y0 + (int) (deltaProjY + 0.5);


            if (X1 > productWidth || X1 > sourceWidth || Y1 > productHeight || Y1 > sourceHeight || X1 < 0 || Y1 < 0) {
                iteratedeltaProjX = deltaProjX / divisor;
                iteratedeltaProjY = deltaProjY / divisor;
                for (int kk = 0; kk < (int) (maxAlt) - 1; kk++) {
                    factor = divisor - kk;
                    X1 = X0 + (int) (factor * iteratedeltaProjX + 0.5);
                    Y1 = Y0 + (int) (factor * iteratedeltaProjY + 0.5);
                    if (X1 < productWidth && X1 < sourceWidth && Y1 < productHeight && Y1 < sourceHeight && X1 > 0 && Y1 > 0) {
                        break;
                    } else {
                        X1 = X0;
                        Y1 = Y0;
                    }
                }
            }
        }

        path[0] = X0;
        path[1] = Y0;
        path[2] = X1;
        path[3] = Y1;

        return path;
    }


    private static void getLinePoints(int x0, int y0, int x1, int y1, int sourceHeight, int sourceWidth,
                                      int targetHeight, int targetWidth, int pathWidth, int pathHeight,
                                      int[] linePointsArray, boolean caseKeyBorder) {

        int x0SourceProduct;
        int y0SourceProduct;
        int x1SourceProduct;
        int y1SourceProduct;
        if (caseKeyBorder) {
            if (x0 == x1 && y0 == y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = 0;
                y1SourceProduct = 0;
            } else if (x0 == x1 && y0 < y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = 0;
                y1SourceProduct = pathHeight;
            } else if (x0 == x1 && y0 > y1) {
                x0SourceProduct = 0;
                y0SourceProduct = targetHeight - 1;
                x1SourceProduct = 0;
                y1SourceProduct = targetHeight - 1 + pathHeight; // pathHeigth <0
            } else if (x0 < x1 && y0 == y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = pathWidth;
                y1SourceProduct = 0;
            } else if (x0 < x1 && y0 < y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = pathWidth;
                y1SourceProduct = pathHeight;
            } else if (x0 < x1 && y0 > y1) {
                x0SourceProduct = 0;
                y0SourceProduct = targetHeight - 1;
                x1SourceProduct = pathWidth;
                y1SourceProduct = targetHeight - 1 + pathHeight; // pathHeigth <0
            } else if (x0 > x1 && y0 == y1) {
                x0SourceProduct = targetWidth - 1;
                y0SourceProduct = 0;
                x1SourceProduct = targetWidth - 1 + pathWidth; // pathWidth <0
                y1SourceProduct = 0;
            } else if (x0 > x1 && y0 < y1) {
                x0SourceProduct = targetWidth - 1;
                y0SourceProduct = 0;
                x1SourceProduct = targetWidth - 1 + pathWidth; // pathWidth <0
                y1SourceProduct = pathHeight;
            } else {
                x0SourceProduct = targetWidth - 1;
                y0SourceProduct = targetHeight - 1;
                x1SourceProduct = targetWidth - 1 + pathWidth; // pathWidth <0
                y1SourceProduct = targetHeight - 1 + pathHeight; // pathHeigth <0
            }
        } else {
            if (x0 == x1 && y0 == y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = 0;
                y1SourceProduct = 0;
            } else if (x0 == x1 && y0 < y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = 0;
                y1SourceProduct = pathHeight;
            } else if (x0 == x1 && y0 > y1) {
                x0SourceProduct = 0;
                y0SourceProduct = sourceHeight - 1;
                x1SourceProduct = 0;
                y1SourceProduct = sourceHeight - 1 + pathHeight; // pathHeight <0
            } else if (x0 < x1 && y0 == y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = pathWidth;
                y1SourceProduct = 0;
            } else if (x0 < x1 && y0 < y1) {
                x0SourceProduct = 0;
                y0SourceProduct = 0;
                x1SourceProduct = pathWidth;
                y1SourceProduct = pathHeight;
            } else if (x0 < x1 && y0 > y1) {
                x0SourceProduct = 0;
                y0SourceProduct = sourceHeight - 1;
                x1SourceProduct = pathWidth;
                y1SourceProduct = sourceHeight - 1 + pathHeight; // pathHeigth <0
            } else if (x0 > x1 && y0 == y1) {
                x0SourceProduct = sourceWidth - 1;
                y0SourceProduct = 0;
                x1SourceProduct = sourceWidth - 1 + pathWidth; // pathWidth <0
                y1SourceProduct = 0;
            } else if (x0 > x1 && y0 < y1) {
                x0SourceProduct = sourceWidth - 1;
                y0SourceProduct = 0;
                x1SourceProduct = sourceWidth - 1 + pathWidth; // pathWidth <0
                y1SourceProduct = pathHeight;
            } else {
                x0SourceProduct = sourceWidth - 1;
                y0SourceProduct = sourceHeight - 1;
                x1SourceProduct = sourceWidth - 1 + pathWidth; // pathWidth <0
                y1SourceProduct = sourceHeight - 1 + pathHeight; // pathHeigth <0
            }
        }
        linePointsArray[0] = x0SourceProduct;
        linePointsArray[1] = y0SourceProduct;
        linePointsArray[2] = x1SourceProduct;
        linePointsArray[3] = y1SourceProduct;

    }

    private static int[] getIndicesPathArray(int x0Center, int y0Center, int x1Center, int y1Center,
                                             int x0, int y0, int ii, int jj, int pathWidth, int pathHeight,
                                             int[] indicesPathArray) {
        int xi;
        int yj;
        int xPath;
        int yPath;

        if (x0Center == x1Center && y0Center == y1Center) {
            xPath = x0;
            yPath = y0;
            xi = 0;
            yj = 0;
        } else if (x0Center == x1Center && y0Center < y1Center) {
            xPath = x0;
            yPath = y0 + jj;
            xi = 0;
            yj = jj;
        } else if (x0Center == x1Center && y0Center > y1Center) {
            xPath = x0;
            yPath = y0 - jj;
            xi = 0;
            yj = pathHeight - jj;
        } else if (x0Center < x1Center && y0Center == y1Center) {
            xPath = x0 + ii;
            yPath = y0;
            xi = ii;
            yj = 0;
        } else if (x0Center < x1Center && y0Center < y1Center) {
            xPath = x0 + ii;
            yPath = y0 + jj;
            xi = ii;
            yj = jj;
        } else if (x0Center < x1Center && y0Center > y1Center) {
            xPath = x0 + ii;
            yPath = y0 - jj;
            xi = ii;
            yj = pathHeight - jj;
        } else if (x0Center > x1Center && y0Center == y1Center) {
            xPath = x0 - ii;
            yPath = y0;
            xi = pathWidth - ii;
            yj = 0;
        } else if (x0Center > x1Center && y0Center < y1Center) {
            xPath = x0 - ii;
            yPath = y0 + jj;
            xi = pathWidth - ii;
            yj = jj;
        } else {
            xPath = x0 - ii;
            yPath = y0 - jj;
            xi = pathWidth - ii;
            yj = pathHeight - jj;
        }

        indicesPathArray[0] = xPath;
        indicesPathArray[1] = yPath;
        indicesPathArray[2] = xi;
        indicesPathArray[3] = yj;

        return indicesPathArray;
    }

}
