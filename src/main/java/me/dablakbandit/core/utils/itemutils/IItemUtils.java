package me.dablakbandit.core.utils.itemutils;

import me.dablakbandit.core.json.JSONArray;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.utils.jsonformatter.JSONFormatter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface IItemUtils{
	
	//@formatter:off
    public boolean hasBanner();

    public Object getNMSCopy(ItemStack is) throws Exception;

    public boolean hasTag(Object is) throws Exception;

    public ItemStack asCraftMirror(Object nis) throws Exception;

    public ItemStack asBukkitCopy(Object nmis) throws Exception;

    public Class<?> getNMSItemClass();

    public String getName(ItemStack is);

    public Object getItem(Object nis) throws Exception;

    public Method getA();

    public String getRawName(ItemStack is);

    public String getItemName(ItemStack is);

    public Object getRegistry();

    public String getMinecraftName(ItemStack is);

    public Object getTag(Object is) throws Exception;

    public void setTag(Object is, Object tag1) throws Exception;

    public boolean isEmpty(Object tag) throws Exception;

    public Object getMap(Object tag) throws Exception;

    public void remove(Object tag, String key) throws Exception;

    public void set(Object tag, String key, Object value) throws Exception;

    public void setString(Object tag, String key, String value) throws Exception;

    public void setShort(Object tag, String key, short value) throws Exception;

    public void setInt(Object tag, String key, int i) throws Exception;

    public void setDouble(Object tag, String key, double d) throws Exception;

    public void setLong(Object tag, String key, long l) throws Exception;

    public boolean hasKey(Object tag, String key) throws Exception;

    public Object get(Object tag, String key) throws Exception;

    public String getString(Object tag, String key) throws Exception;

    public int getInt(Object tag, String key) throws Exception;

    public double getDouble(Object tag, String key) throws Exception;

    public long getLong(Object tag, String key) throws Exception;

    public short getShort(Object tag, String key) throws Exception;

    public Object getNewNBTTagCompound() throws Exception;

    public boolean hasAttributeModifiersKey(Object tag) throws Exception;

    public Object getList(Object tag) throws Exception;

    public Object getList(Object tag, String name, int id) throws Exception;

    public boolean getUnbreakable(Object tag) throws Exception;

    public void setUnbreakable(Object tag, boolean value) throws Exception;

    public Object getNewNBTTagList() throws Exception;

    public void addToList(Object taglist, Object nbt) throws Exception;

    public int getSize(Object list) throws Exception;

    public Object get(Object tlist, int i) throws Exception;

    public Object getNewNBTTagByte(byte value) throws Exception;

    public Object getNewNBTTagByteArray(byte[] value) throws Exception;

    public Object getData(Object nbt) throws Exception;

    public Object createData(Object value) throws Exception;

    public Map<String, Object> convertCompoundTagToValueMap(Object nbt) throws Exception;

    public List<Object> convertListTagToValueList(Object nbttl) throws Exception;

    public Object convertValueMapToCompoundTag(Map<String, Object> map) throws Exception;

    public Object convertValueListToListTag(List<Object> list) throws Exception;

    @Deprecated
    public void convertListTagToJSON(Object nbttl, JSONArray ja, JSONArray helper) throws Exception;

    public void convertListTagToJSON(Object nbttl, JSONArray ja) throws Exception;

    @Deprecated
    public void convertCompoundTagToJSON(Object nbt, JSONObject jo, JSONObject helper) throws Exception;

    public void convertCompoundTagToJSON(Object nbt, JSONObject jo) throws Exception;

    @Deprecated
    public Object convertJSONToCompoundTag(JSONObject jo, JSONObject helper) throws Exception;

    public Object convertJSONToCompoundTag(JSONObject jo) throws Exception;

    @Deprecated
    public Object convertJSONToListTag(JSONArray ja, JSONArray helper) throws Exception;

    public Object convertJSONToListTag(JSONArray ja) throws Exception;

    @Deprecated
    public Object getDataJSON(String key, Object nbt, JSONObject jo, JSONObject helper) throws Exception;

    public JSONArray getDataJSON(Object nbt) throws Exception;

    @Deprecated
    public Object getDataJSON(Object nbt, JSONArray ja, JSONArray helper) throws Exception;

    @Deprecated
    public Object createDataJSON(String key, JSONObject jo, JSONObject helper) throws Exception;

    public Object createDataJSON(String key, JSONObject jo) throws Exception;

    public byte getByte(Object o);

    public short getShort(Object o);

    public int getInt(Object o);

    public double getDouble(Object o);

    public float getFloat(Object o);

    public long getLong(Object o);

    @Deprecated
    public Object createDataJSON(int key, JSONArray jo, JSONArray helper) throws Exception;

    public Object createDataJSON(int key, JSONArray jo) throws Exception;

    public boolean compareBaseTag(Object tag, Object tag1) throws Exception;

    public boolean compareCompoundTag(Object tag, Object tag1) throws Exception;

    public boolean compareListTag(Object tag, Object tag1) throws Exception;

    public boolean compare(ItemStack is1, ItemStack is2);

    public boolean canMerge(ItemStack add, ItemStack to);

    public boolean isModified(ItemStack is);

    public void sortByMaterial(List<ItemStack> items);

    public void sortByName(List<ItemStack> items);

    public void sortByAmount(List<ItemStack> items);

    public ItemStack convertJSONToItemStack(JSONObject jo) throws Exception;

    public JSONObject convertItemStackToJSON(ItemStack is) throws Exception;

    public Material getMaterial(String... possible);

    public Object getEmpty();

    static void fixTags(JSONObject jo1) throws Exception{
        if(jo1.has("display")){
            JSONArray ja2 = jo1.getJSONArray("display");
            JSONObject jo2 = ja2.getJSONObject(1);
            if(jo2.has("Name")){
                JSONArray ja3 = jo2.getJSONArray("Name");
                Object o = ja3.get(1);
                if(o instanceof String){
                    try {
                        JSONObject parsed = new JSONObject((String) o);
                        //FINE
                    }catch (Exception e) {
                        try {
                            JSONObject parsed = new JSONObject((String) o);
                            Object extra = parsed.opt("extra");
                            if (extra instanceof JSONArray) {
                                JSONArray array = (JSONArray) extra;
                                if (array.length() > 0) {
                                    JSONObject check = array.getJSONObject(0);
                                    Object text = check.opt("text");
                                    if (text instanceof String) {
                                        try {
                                            JSONArray fix = new JSONArray((String) text);
                                            if (fix.length() > 0) {
                                                ja3.remove(1);
                                                ja3.put(fix.toString());
                                            }
                                        } catch (Exception e1) {
                                        }
                                    }
                                }
                            }
                        } catch (Exception e1) {
                            e.printStackTrace();
                            JSONFormatter jf = new JSONFormatter();
                            jf.append((String) o);
                            ja3.remove(1);
                            ja3.put(jf.toJSON());
                        }
                    }
                }
            }
        }
        if(jo1.has("ench")){
            fixEnchantments(jo1.getJSONArray("ench").getJSONArray(1));
        }
        if(jo1.has("StoredEnchantments")){
            fixEnchantments(jo1.getJSONArray("StoredEnchantments").getJSONArray(1));
        }
    }

    static void fixEnchantments(JSONArray ja) throws Exception{
        for(int i = 0; i < ja.length(); i++){
            JSONArray ja2 = ja.getJSONArray(i);
            JSONObject jo = ja2.getJSONObject(1);
            if(jo.has("id")){
                JSONArray ja1 = jo.getJSONArray("id");
                if(ja1.getInt(0) == 2){
                    int id = ja1.getInt(1);
                    EnchantmentFixer ef = EnchantmentFixer.match(id);
                    if(ef != null){
                        ja1.remove(0);
                        ja1.remove(0);
                        ja1.put(8);
                        ja1.put(ef.getMcname());
                    }
                }
            }
        }
    }
}
