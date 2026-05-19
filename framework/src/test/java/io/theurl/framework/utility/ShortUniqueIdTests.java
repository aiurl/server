package io.theurl.framework.utility;

import org.junit.jupiter.api.Test;

class ShortUniqueIdTests {
    @Test
    void testEncodeIntToStringId() {
        var id = ShortUniqueId.getDefault().encode(1000);
        assert id.equals("gN3");
    }

    @Test
    void testDecodeFromStringId() {
        var originalId = ShortUniqueId.getDefault().decode("gp26");
        assert originalId.length ==1;
        assert originalId[0] == 10000;
    }
}
