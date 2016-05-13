package sdk.grayweb.com.slientsdk.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by engine on 16/5/11.
 */
@DatabaseTable(tableName = "openrecord")
public class OpenPackage {
    @DatabaseField
    public String name ;
    @DatabaseField
    public String packageName ;
    @DatabaseField
    public long opentime ;
    public OpenPackage(){}
}
