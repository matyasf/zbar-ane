package com.blogspot.visualscripts.zbar{

public class ZbarSymbolSetting {

    /** Enable or disable setting */
    public static const ENABLE : ZbarSymbolSetting = new ZbarSymbolSetting(0);
    /** Enable check digit when optional. */
    public static const ADD_CHECK : ZbarSymbolSetting = new ZbarSymbolSetting(1);
    /** Return check digit when present. */
    public static const EMIT_CHECK : ZbarSymbolSetting = new ZbarSymbolSetting(2);
    /** Enable full ASCII character set. */
    public static const ASCII : ZbarSymbolSetting = new ZbarSymbolSetting(3);
    /** Minimum data length for valid decode. */
    public static const MIN_LEN : ZbarSymbolSetting = new ZbarSymbolSetting(0x20);
    /** Maximum data length for valid decode. */
    public static const MAX_LEN : ZbarSymbolSetting = new ZbarSymbolSetting(0x21);
    /** Required video consistency frames. */
    public static const UNCERTAINTY : ZbarSymbolSetting = new ZbarSymbolSetting(0x40);
    /** Enable scanner to collect position data. */
    public static const POSITION : ZbarSymbolSetting = new ZbarSymbolSetting(0x80);
    /** Image scanner vertical scan density. Default is 3 */
    public static const X_DENSITY : ZbarSymbolSetting = new ZbarSymbolSetting(0x100);
    /** Image scanner horizontal scan density. Default is 3*/
    public static const Y_DENSITY : ZbarSymbolSetting = new ZbarSymbolSetting(0x101);

    private var _setting : uint;

    public function ZbarSymbolSetting(zSetting:uint)
    {
        _setting = zSetting;
    }

    public function get setting() : uint
    {
        return _setting;
    }

}
}