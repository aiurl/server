package io.theurl.framework.utility;

import org.junit.jupiter.api.Test;

class ShortUniqueIdTests {
    @Test
    void generate() {
        var id = ShortUniqueId.getDefault().encode(1000);
        assert id.equals("gN3");
    }
}
