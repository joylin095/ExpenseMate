package com.example.expensemate.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocationTest {

    private Location location;

    @Before
    public void setUp() {
        location = new Location();
    }

    @Test
    // 第一次調用 analyzeLocation 應該返回 false
    public void testFirstCallReturnsFalse() {
        boolean result = location.analyzeLocation(25.0330, 121.5654); // 台北101
        assertFalse(result);
    }

    @Test
    // 在相同位置第二次調用應該返回 true
    public void testSameLocationSecondCallReturnsTrue() {
        double lat = 25.0330;
        double lon = 121.5654;

        // 第一次調用
        boolean firstResult = location.analyzeLocation(lat, lon);
        assertFalse(firstResult);

        // 第二次調用相同位置
        boolean secondResult = location.analyzeLocation(lat, lon);
        assertTrue(secondResult);
    }

    @Test
    // 在相同位置第三次調用應該返回 false
    public void testSameLocationThirdCallReturnsFalse() {
        double lat = 25.0330;
        double lon = 121.5654;

        // 第一次和第二次調用
        location.analyzeLocation(lat, lon);
        location.analyzeLocation(lat, lon);

        // 第三次調用相同位置
        boolean thirdResult = location.analyzeLocation(lat, lon);
        assertFalse(thirdResult);
    }

    @Test
    // 測試距離閾值邊界情況
    public void testDistanceThresholdBoundary() {
        double lat1 = 25.0330;
        double lon1 = 121.5654;

        // 第一次調用
        location.analyzeLocation(lat1, lon1);

        // 計算距離剛好小於閾值的位置 (約0.094公里)
        double lat2 = lat1 + 0.0006; // 大約66米
        double lon2 = lon1 + 0.0006;

        boolean result = location.analyzeLocation(lat2, lon2);
        assertTrue(result);
    }

    @Test
   // 測試距離超過閾值的情況
    public void testDistanceExceedsThreshold() {
        double lat1 = 25.0330;
        double lon1 = 121.5654;

        // 第一次調用
        location.analyzeLocation(lat1, lon1);

        // 計算距離超過閾值的位置 (約0.2公里)
        double lat2 = lat1 + 0.002; // 大約200米
        double lon2 = lon1 + 0.002;

        boolean result = location.analyzeLocation(lat2, lon2);
        assertFalse(result);
    }

    @Test
    // 移動到新位置後重置通知狀態
    public void testLocationChangeResetsNotificationState() {
        double lat1 = 25.0330;
        double lon1 = 121.5654;
        double lat2 = 25.0430; // 約1公里外
        double lon2 = 121.5754;

        // 在第一個位置觸發通知
        location.analyzeLocation(lat1, lon1);
        location.analyzeLocation(lat1, lon1); // 觸發通知

        // 移動到新位置
        boolean moveResult = location.analyzeLocation(lat2, lon2);
        assertFalse(moveResult);

        // 在新位置應該能再次觸發通知
        boolean newLocationResult = location.analyzeLocation(lat2, lon2);
        assertTrue(newLocationResult);
    }

    @Test
    // 測試零座標
    public void testZeroCoordinates() {
        location.analyzeLocation(25.0330, 121.5654);

        boolean result = location.analyzeLocation(0.0, 0.0);
        assertFalse(result);

        boolean secondResult = location.analyzeLocation(0.0, 0.0);
        assertTrue(secondResult);
    }

    @Test
    // 測試負座標
    public void testNegativeCoordinates() {
        double lat = -25.0330;
        double lon = -121.5654;

        boolean result = location.analyzeLocation(lat, lon);
        assertFalse(result);

        boolean secondResult = location.analyzeLocation(lat, lon);
        assertTrue(secondResult);
    }

    @Test
    // 測試極大座標值
    public void testLargeCoordinates() {
        double lat = 89.9999; // 接近北極
        double lon = 179.9999; // 接近國際換日線

        boolean result = location.analyzeLocation(lat, lon);
        assertFalse(result);

        boolean secondResult = location.analyzeLocation(lat, lon);
        assertTrue(secondResult);
    }

    @Test
    // 測試連續的位置變化
    public void testContinuousLocationChanges() {
        // 模擬移動路徑
        double[][] locations = {
                {25.0330, 121.5654}, // 台北101
                {25.0430, 121.5754}, // 移動約1公里
                {25.0530, 121.5854}, // 再移動約1公里
                {25.0530, 121.5854}  // 停留在同一位置
        };

        // 第一個位置
        assertFalse(location.analyzeLocation(locations[0][0], locations[0][1]));

        // 移動到第二個位置
        assertFalse(location.analyzeLocation(locations[1][0], locations[1][1]));

        // 移動到第三個位置
        assertFalse(location.analyzeLocation(locations[2][0], locations[2][1]));

        // 停留在第三個位置
        assertTrue(location.analyzeLocation(locations[3][0], locations[3][1]));
    }

    @Test
    // 測試距離計算精度
    public void testDistanceCalculationPrecision() {
        double lat1 = 25.0330;
        double lon1 = 121.5654;

        // 第一次調用
        location.analyzeLocation(lat1, lon1);

        // 非常小的位置變化 (約10米)
        double lat2 = lat1 + 0.0001;
        double lon2 = lon1 + 0.0001;

        boolean result = location.analyzeLocation(lat2, lon2);
        assertTrue(result);
    }
}