package com.blogspot.visualscripts.zbar{

public class ZbarSymbolType {

    public static const ALL : ZbarSymbolType = new ZbarSymbolType(0);
    /** Symbol detected but not decoded. No idea what this does, use at your own peril */
    public static const PARTIAL : ZbarSymbolType = new ZbarSymbolType(1);
    public static const EAN8 : ZbarSymbolType = new ZbarSymbolType(8);
    public static const UPCE : ZbarSymbolType = new ZbarSymbolType(9);
    /** ISBN-10 (from EAN-13). */
    public static const ISBN10 : ZbarSymbolType = new ZbarSymbolType(10);
    public static const UPCA : ZbarSymbolType = new ZbarSymbolType(12);
    public static const EAN13 : ZbarSymbolType = new ZbarSymbolType(13);
    /** ISBN-13 (from EAN-13). */
    public static const ISBN13 : ZbarSymbolType = new ZbarSymbolType(14);
    /** Interleaved 2 of 5. */
    public static const I25 : ZbarSymbolType = new ZbarSymbolType(25);
    /** DataBar (RSS-14). */
    public static const DATABAR : ZbarSymbolType = new ZbarSymbolType(34);
    public static const DATABAR_EXP : ZbarSymbolType = new ZbarSymbolType(35);
    public static const CODABAR : ZbarSymbolType = new ZbarSymbolType(38);
    public static const CODE39 : ZbarSymbolType = new ZbarSymbolType(39);
    public static const PDF417 : ZbarSymbolType = new ZbarSymbolType(57);
    public static const QRCODE : ZbarSymbolType = new ZbarSymbolType(64);
    public static const CODE93 : ZbarSymbolType = new ZbarSymbolType(93);
    public static const CODE128 : ZbarSymbolType = new ZbarSymbolType(128);

    private var _type : uint;

    public function ZbarSymbolType(sType:uint)
    {
        _type = sType;
    }

    public function get symbolType() : uint
    {
        return _type;
    }

}
}