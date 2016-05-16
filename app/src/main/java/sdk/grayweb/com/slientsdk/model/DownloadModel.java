package sdk.grayweb.com.slientsdk.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by engine on 16/5/11.
 */
@DatabaseTable(tableName = "download")
public class DownloadModel {
    @DatabaseField
    public String name ;
    @DatabaseField
    public String packagename ;
    @DatabaseField
    public String downloadpath ;
    @DatabaseField
    public  int status ;
    @DatabaseField
    public int downloadtime ;
    public  int needopentime ;
    @DatabaseField
    public int level ;
    @DatabaseField
    public int price ;
    public DownloadModel(){}
}
