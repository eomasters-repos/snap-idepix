package org.esa.s2tbx.s2msi.idepix.operators.cloudshadow;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

/**
 * @author Tonio Fincke
 */
public class ClusteringKMeansTest {

    private final static double[] band1Values = new double[]{0.15629999339580536, 0.03359999880194664, 0.026100000366568565,
            0.025100000202655792, 0.022199999541044235, 0.019700000062584877, 0.02019999921321869, 0.02290000021457672,
            0.02449999935925007, 0.025800000876188278, 0.025499999523162842, 0.02590000070631504, 0.02879999950528145,
            0.028200000524520874, 0.028300000354647636, 0.02759999968111515, 0.025499999523162842, 0.03060000017285347,
            0.02810000069439411, 0.030300000682473183, 0.02590000070631504, 0.024700000882148743, 0.02630000002682209,
            0.02280000038444996, 0.022600000724196434, 0.023399999365210533, 0.027300000190734863, 0.02590000070631504,
            0.029200000688433647, 0.029400000348687172, 0.026599999517202377, 0.027499999850988388, 0.028200000524520874,
            0.02419999986886978, 0.025200000032782555, 0.03060000017285347, 0.02850000001490116, 0.029100000858306885,
            0.026000000536441803, 0.026100000366568565, 0.026000000536441803, 0.02800000086426735, 0.027899999171495438,
            0.027000000700354576, 0.025800000876188278, 0.025299999862909317, 0.023900000378489494, 0.02370000071823597,
            0.027799999341368675, 0.027400000020861626, 0.025299999862909317, 0.025299999862909317, 0.025200000032782555,
            0.02319999970495701, 0.022700000554323196, 0.022199999541044235, 0.022600000724196434, 0.02250000089406967,
            0.023600000888109207, 0.023000000044703484, 0.022700000554323196, 0.021400000900030136, 0.021299999207258224,
            0.025499999523162842, 0.02800000086426735, 0.02669999934732914, 0.02630000002682209, 0.02969999983906746,
            0.02459999918937683, 0.02500000037252903, 0.025499999523162842, 0.023800000548362732, 0.03920000046491623,
            0.21480000019073486, 0.2782000005245209, 0.27079999446868896, 0.2508000135421753, 0.19220000505447388,
            0.14110000431537628, 0.1039000004529953, 0.0640999972820282, 0.06710000336170197, 0.03220000118017197,
            0.024900000542402267, 0.02410000003874302, 0.021400000900030136, 0.025699999183416367, 0.029999999329447746,
            0.08190000057220459, 0.12759999930858612, 0.21439999341964722, 0.2689000070095062, 0.29409998655319214,
            0.3003999888896942, 0.30300000309944153, 0.29989999532699585, 0.2741999924182892, 0.1891999989748001,
            0.1995999962091446, 0.23240000009536743};

    private final static double[] band2Values = new double[]{0.1392, 0.1359, 0.1048, 0.0971, 0.0905, 0.1009, 0.0989,
            0.0991, 0.0931, 0.078, 0.0855, 0.089, 0.0804, 0.0796, 0.0986, 0.1303, 0.1547, 0.1078, 0.1795, 0.2242,
            0.2268, 0.238, 0.2207, 0.194, 0.206, 0.2173, 0.182, 0.2168, 0.1733, 0.1528, 0.1408, 0.1445, 0.1383, 0.1166,
            0.1225, 0.1403, 0.1716, 0.18, 0.188, 0.1886, 0.1634, 0.1126, 0.0988, 0.0987, 0.0992, 0.1064, 0.1129,
            0.0934, 0.0917, 0.0869, 0.086, 0.0881, 0.0937, 0.0945, 0.0869, 0.0834, 0.0865, 0.0866, 0.095, 0.0896,
            0.0926, 0.0855, 0.0933, 0.0891, 0.0886, 0.0941, 0.0965, 0.0876, 0.087, 0.0853, 0.0796, 0.0712, 0.0648,
            0.0622, 0.0637, 0.0625, 0.0668, 0.0688, 0.0719, 0.0693, 0.0702, 0.0738, 0.0764, 0.0949, 0.0971, 0.0929,
            0.0897, 0.0968, 0.1007, 0.0897, 0.0848, 0.0974, 0.1069, 0.1033, 0.0968, 0.0977, 0.1069, 0.0947, 0.0948,
            0.0961};
    private ClusteringKMeans clusteringKMeans;

    @Before
    public void setUp() {
        S2IdepixCloudShadowOp.clusterCountDefine = 3;
        clusteringKMeans = new ClusteringKMeans();
    }

    @Test
    public void computedKMeansCluster_singleBand() throws Exception {
        S2IdepixCloudShadowOp.SENSOR_BAND_CLUSTERING = 1;

        final double[][] doubles = clusteringKMeans.computedKMeansCluster(band1Values);

        double[] expectedDoubles = new double[]{0.027628395223507175, 0.26682499796152115, 0.1585571425301688};

        assertEquals(expectedDoubles[0], doubles[0][0], 1e-8);
        assertEquals(expectedDoubles[1], doubles[1][0], 1e-8);
        assertEquals(expectedDoubles[2], doubles[2][0], 1e-8);

//        final StringBuilder outputBuilder = new StringBuilder("new double[]{");
//        for (int i = 0; i < doubles.length; i++) {
//            outputBuilder.append(doubles[i][0]);
//            if (i < doubles.length - 1) {
//                outputBuilder.append(", ");
//            }
//        }
//        outputBuilder.append("};");
//        System.out.println(outputBuilder.toString());
    }

    @Test
    public void computedKMeansCluster_multiBand() throws Exception {
        S2IdepixCloudShadowOp.SENSOR_BAND_CLUSTERING = 2;

        final double[][] doubles = clusteringKMeans.computedKMeansCluster(band1Values, band2Values);

        final double[][] expectedDoubles = new double[][]{{0.030700000090291724, 0.09508749999999999},
                {0.24001764549928553, 0.08908823529411765}, {0.0265578951098417, 0.19064210526315792}};

        assertEquals(expectedDoubles.length, doubles.length);
        for (int i = 0; i < doubles.length; i++) {
            assertArrayEquals(expectedDoubles[i], doubles[i], 1e-8);
        }

//        final StringBuilder outputBuilder = new StringBuilder("new double[][]{");
//        for (int i = 0; i < doubles.length; i++) {
//            outputBuilder.append("{");
//            for (int j = 0; j < doubles[i].length; j++) {
//                outputBuilder.append(doubles[i][j]);
//                if (j < doubles[i].length - 1) {
//                    outputBuilder.append(", ");
//                }
//            }
//            outputBuilder.append("}");
//            if (i < doubles.length - 1) {
//                outputBuilder.append(", ");
//            }
//        }
//        outputBuilder.append("};");
//        System.out.println(outputBuilder.toString());
    }

}