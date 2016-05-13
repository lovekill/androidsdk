package sdk.grayweb.com.slientsdk.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by engine on 16/5/11.
 */
@DatabaseTable(tableName = "action")
public class ActionModel {
    @DatabaseField
    public int type ;//操作类型 1下载，2安装 3打开
    @DatabaseField
    public String packageName ;
    @DatabaseField
    public long time;
    public ActionModel(){}
}
