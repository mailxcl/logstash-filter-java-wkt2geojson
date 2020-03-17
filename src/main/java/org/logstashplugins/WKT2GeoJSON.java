package org.logstashplugins;

import co.elastic.logstash.api.Configuration;
import co.elastic.logstash.api.Context;
import co.elastic.logstash.api.Event;
import co.elastic.logstash.api.Filter;
import co.elastic.logstash.api.FilterMatchListener;
import co.elastic.logstash.api.LogstashPlugin;
import co.elastic.logstash.api.PluginConfigSpec;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// class name must match plugin name
@LogstashPlugin(name = "wkt2geojson")
public class WKT2GeoJSON implements Filter {

    public static final PluginConfigSpec<String> SOURCE_CONFIG =
            PluginConfigSpec.requiredStringSetting("source");
    public static final PluginConfigSpec<String> TARGET_CONFIG =
            PluginConfigSpec.requiredStringSetting("target");

    private String id;
    private String sourceField;
    private String targetField;

    public WKT2GeoJSON(String id, Configuration config, Context context) {
        // constructors should validate configuration options
        this.id = id;
        this.sourceField = config.get(SOURCE_CONFIG);
        this.targetField = config.get(TARGET_CONFIG);
    }

    @Override
    public Collection<Event> filter(Collection<Event> events, FilterMatchListener matchListener) {
        for (Event e : events) {
            Object f = e.getField(this.sourceField);
            if (f instanceof String) {

                String wkt = (String)f;

                String geoJson = null;
                try {
                    WKTReader reader = new WKTReader();
                    Geometry geometry = reader.read(wkt);
                    StringWriter writer = new StringWriter();
                    GeometryJSON g = new GeometryJSON();
                    g.write(geometry, writer);
                    geoJson = writer.toString();
                    writer.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.setField(this.targetField, geoJson);
                matchListener.filterMatched(e);
            }
        }
        return events;
    }

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        // should return a list of all configuration options for this plugin
        List<PluginConfigSpec<?>> list = new ArrayList<>();
        list.add(SOURCE_CONFIG);
        list.add(TARGET_CONFIG);
        return Collections.synchronizedList(list);
    }

    @Override
    public String getId() {
        return this.id;
    }
}
