package xuanfs.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Suning {

    private String attractId;
    private int attractType;
    private String brandCode;
    private int categCode;
    private String categName;
    private  int chanVal;
    private int columnId;
    private String commListImgurl;
    private int dataSrc;
    private String defSubComm;
    private String encryptGrppurId;
    private int field1;
    private int field2;
    private Date previewBegindt;
    private Date previewEnddt;
    private Date gbBegindate;
    private String gbCommHot;
    private int gbCommNum;
    private Date gbEnddate;
    private float gbPrice;
    private String grppurId;
    private String grppurName;
    private int limitBuyNum;
    private int markfordelete;
    private String partName;
    private String partnumber;
    private String partType;
    private float payPrice;
    private String preGroupbuyId;
    private int showCountType;
    private int status;
    private int taskUpLength;
    private int vendorCode;
    private String vendorName;
    private String newCommImgUrl;

    private int isSetmeal;
    private int productType;
    private String gbBeginDateStr;
    private String gbEndDateStr;
    private int isSuper;
    private List labelList;
}
