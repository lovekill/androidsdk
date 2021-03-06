package sdk.grayweb.com.slientsdk.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by engine on 16/5/11.
 */
@DatabaseTable(tableName = "apkmodel")
public class ApkModel {
    @DatabaseField(unique = true,id = true)
    public int id ;
    @DatabaseField
    public  String name ;
    @DatabaseField(index = true ,unique = true)
    public String packagename ;
    @DatabaseField
    public int status ;
    @DatabaseField
    public long createTime ;
    @DatabaseField
    public int needopentime ;
    @DatabaseField
    public String apkpath ;
    @DatabaseField
    public int level;
    @DatabaseField
    public int dayopentimes;
    public  ApkModel(){}
}