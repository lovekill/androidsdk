package sdk.grayweb.com.slientsdk.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by engine on 16/5/11.
 */
@DatabaseTable(tableName = "download")
public class DownloadModel {
    @DatabaseField
    public String packagename ;
    @DatabaseField
    public String downloadpath ;
    @DatabaseField
    public long downloadtime ;
    @DatabaseField
    public int level ;
    public DownloadModel(){}
}
