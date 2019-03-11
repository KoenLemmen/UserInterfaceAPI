/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package org.spookit.api.userinterface.externalcore.comphenix;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.base.Splitter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import com.google.common.primitives.Primitives;

@SuppressWarnings({"unchecked","rawtypes"})
public class NbtFactory {
    private static final BiMap<Integer, Class<?>> NBT_CLASS = HashBiMap.create();
    private static final BiMap<Integer, NbtType> NBT_ENUM = HashBiMap.create();
    private Class<?> BASE_CLASS;
    private Class<?> COMPOUND_CLASS;
    private Method NBT_CREATE_TAG;
    private Method NBT_GET_TYPE;
    private Field NBT_LIST_TYPE;
    private final Field[] DATA_FIELD = new Field[12];
    private Class<?> CRAFT_STACK;
    private Field CRAFT_HANDLE;
    private Field STACK_TAG;
    private static NbtFactory INSTANCE;

    private static NbtFactory get() {
        if (INSTANCE == null) {
            INSTANCE = new NbtFactory();
        }
        return INSTANCE;
    }

    private NbtFactory() {
        if (this.BASE_CLASS == null) {
            try {
                ClassLoader loader = NbtFactory.class.getClassLoader();
                String packageName = Bukkit.getServer().getClass().getPackage().getName();
                Class offlinePlayer = loader.loadClass(packageName + ".CraftOfflinePlayer");
                this.COMPOUND_CLASS = NbtFactory.getMethod(0, 8, offlinePlayer, "getData", new Class[0]).getReturnType();
                this.BASE_CLASS = this.COMPOUND_CLASS.getSuperclass();
                this.NBT_GET_TYPE = NbtFactory.getMethod(0, 8, this.BASE_CLASS, "getTypeId", new Class[0]);
                this.NBT_CREATE_TAG = NbtFactory.getMethod(8, 0, this.BASE_CLASS, "createTag", Byte.TYPE);
                this.CRAFT_STACK = loader.loadClass(packageName + ".inventory.CraftItemStack");
                this.CRAFT_HANDLE = NbtFactory.getField(null, this.CRAFT_STACK, "handle");
                this.STACK_TAG = NbtFactory.getField(null, this.CRAFT_HANDLE.getType(), "tag");
            }
            catch (ClassNotFoundException e) {
                throw new IllegalStateException("Unable to find offline player.", e);
            }
        }
    }

    private Map<String, Object> getDataMap(Object handle) {
        return (Map)NbtFactory.getFieldValue(this.getDataField(NbtType.TAG_COMPOUND, handle), handle);
    }

    private List<Object> getDataList(Object handle) {
        return (List)NbtFactory.getFieldValue(this.getDataField(NbtType.TAG_LIST, handle), handle);
    }

    public static  NbtList createList(Object ... content) {
        return NbtFactory.createList(Arrays.asList(content));
    }

    public static NbtList createList(Iterable<? extends Object> iterable) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        NbtList list = nbtFactory.new NbtList(INSTANCE.createNbtTag(NbtType.TAG_LIST, "", null));
        for (Object obj : iterable) {
            list.add(obj);
        }
        return list;
    }

    public static NbtCompound createCompound() {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtCompound(INSTANCE.createNbtTag(NbtType.TAG_COMPOUND, "", null));
    }

    public static NbtCompound createRootCompound(String name) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtCompound(INSTANCE.createNbtTag(NbtType.TAG_COMPOUND, name, null));
    }

    public static NbtList fromList(Object nmsList) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtList(nmsList);
    }

    public static NbtCompound fromCompound(Object nmsCompound) {
        NbtFactory nbtFactory = NbtFactory.get();
        nbtFactory.getClass();
        return nbtFactory.new NbtCompound(nmsCompound);
    }

    public static void setItemTag(ItemStack stack, NbtCompound compound) {
    	stack = getCraftItemStack(stack);
        NbtFactory.checkItemStack(stack);
        Object nms = NbtFactory.getFieldValue(NbtFactory.get().CRAFT_HANDLE, (Object)stack);
        NbtFactory.setFieldValue(NbtFactory.get().STACK_TAG, nms, compound.getHandle());
    }

    public static NbtCompound fromItemTag(ItemStack stack) {
    	stack = getCraftItemStack(stack);
        NbtFactory.checkItemStack(stack);
        Object nms = NbtFactory.getFieldValue(NbtFactory.get().CRAFT_HANDLE, (Object)stack);
        Object tag = NbtFactory.getFieldValue(NbtFactory.get().STACK_TAG, nms);
        if (tag == null) {
            NbtCompound compound = NbtFactory.createRootCompound("tag");
            NbtFactory.setItemTag(stack, compound);
            return compound;
        }
        return NbtFactory.fromCompound(tag);
    }

    public static ItemStack getCraftItemStack(ItemStack stack) {
        if (stack == null || NbtFactory.get().CRAFT_STACK.isAssignableFrom(stack.getClass())) {
            return stack;
        }
        try {
            Constructor caller = NbtFactory.INSTANCE.CRAFT_STACK.getDeclaredConstructor(ItemStack.class);
            caller.setAccessible(true);
            return (ItemStack)caller.newInstance(new Object[]{stack});
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to convert " + (Object)stack + " + to a CraftItemStack.");
        }
    }

    public static boolean isCraftItemStack(ItemStack stack) {
    	return NbtFactory.get().CRAFT_STACK.isAssignableFrom(stack.getClass());
    }
    private static void checkItemStack(ItemStack stack) {
        if (stack == null) {
            throw new IllegalArgumentException("Stack cannot be NULL.");
        }
        if (!NbtFactory.get().CRAFT_STACK.isAssignableFrom(stack.getClass())) {
            throw new IllegalArgumentException("Stack must be a CraftItemStack, found " + stack.getClass().getSimpleName());
        }
        if (stack.getType() == Material.AIR) {
            throw new IllegalArgumentException("ItemStacks representing air cannot store NMS information.");
        }
    }

    private Object unwrapValue(String name, Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Wrapper) {
            return ((Wrapper)value).getHandle();
        }
        if (value instanceof List) {
            throw new IllegalArgumentException("Can only insert a WrappedList.");
        }
        if (value instanceof Map) {
            throw new IllegalArgumentException("Can only insert a WrappedCompound.");
        }
        return this.createNbtTag(this.getPrimitiveType(value), name, value);
    }

    private Object wrapNative(Object nms) {
        if (nms == null) {
            return null;
        }
        if (this.BASE_CLASS.isAssignableFrom(nms.getClass())) {
            NbtType type = this.getNbtType(nms);
            switch (type) {
                case TAG_COMPOUND: {
                    return new NbtCompound(nms);
                }
                case TAG_LIST: {
                    return new NbtList(nms);
                }
                default:
            }
            return NbtFactory.getFieldValue(this.getDataField(type, nms), nms);
        }
        throw new IllegalArgumentException("Unexpected type: " + nms);
    }

    private Object createNbtTag(NbtType type, String name, Object value) {
        Object tag = NbtFactory.invokeMethod(this.NBT_CREATE_TAG, null, Byte.valueOf((byte)type.id));
        if (value != null) {
            NbtFactory.setFieldValue(this.getDataField(type, tag), tag, value);
        }
        return tag;
    }

    private Field getDataField(NbtType type, Object nms) {
        if (this.DATA_FIELD[type.id] == null) {
            this.DATA_FIELD[type.id] = NbtFactory.getField(nms, null, type.getFieldName());
        }
        return this.DATA_FIELD[type.id];
    }

    private NbtType getNbtType(Object nms) {
        byte type = ((Byte)NbtFactory.invokeMethod(this.NBT_GET_TYPE, nms, new Object[0])).byteValue();
        return (NbtType)((Object)NBT_ENUM.get((Object)Integer.valueOf(type)));
    }

    private NbtType getPrimitiveType(Object primitive) {
        NbtType type = (NbtType)((Object)NBT_ENUM.get(NBT_CLASS.inverse().get((Object)Primitives.unwrap(primitive.getClass()))));
        if (type == null) {
            throw new IllegalArgumentException(String.format("Illegal type: %s (%s)", primitive.getClass(), primitive));
        }
        return type;
    }

    private static  Object invokeMethod(Method method, Object target, Object ... params) {
        try {
            return method.invoke(target, params);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to invoke method " + method + " for " + target, e);
        }
    }

    private static void setFieldValue(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to set " + field + " for " + target, e);
        }
    }

    private static Object getFieldValue(Field field, Object target) {
        try {
            return field.get(target);
        }
        catch (Exception e) {
            throw new RuntimeException("Unable to retrieve " + field + " for " + target, e);
        }
    }

    private static  Method getMethod(int requireMod, int bannedMod, Class<?> clazz, String methodName, Class<?> ... params) {
        for (Method method : clazz.getDeclaredMethods()) {
            if ((method.getModifiers() & requireMod) != requireMod || (method.getModifiers() & bannedMod) != 0 || methodName != null && !method.getName().equals(methodName) || !Arrays.equals(method.getParameterTypes(), params)) continue;
            method.setAccessible(true);
            return method;
        }
        if (clazz.getSuperclass() != null) {
            return NbtFactory.getMethod(requireMod, bannedMod, clazz.getSuperclass(), methodName, params);
        }
        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
    }

    private static Field getField(Object instance, Class<?> clazz, String fieldName) {
        if (clazz == null) {
            clazz = instance.getClass();
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.getName().equals(fieldName)) continue;
            field.setAccessible(true);
            return field;
        }
        if (clazz.getSuperclass() != null) {
            return NbtFactory.getField(instance, clazz.getSuperclass(), fieldName);
        }
        throw new IllegalStateException("Unable to find field " + fieldName + " in " + instance);
    }

    static  NbtType access$1400(NbtFactory x0, Object x1) {
        return x0.getNbtType(x1);
    }

    private class ConvertedList
    extends AbstractList<Object>
    implements Wrapper {
        private final Object handle;
        private final List<Object> original;
        private final CachedNativeWrapper cache;

        public ConvertedList(Object handle, List<Object> original) {
            this.cache = new CachedNativeWrapper();
            if (NbtFactory.this.NBT_LIST_TYPE == null) {
                NbtFactory.this.NBT_LIST_TYPE = NbtFactory.getField(handle, null, "type");
            }
            this.handle = handle;
            this.original = original;
        }

        protected Object wrapOutgoing(Object value) {
            return this.cache.wrap(value);
        }

        protected Object unwrapIncoming(Object wrapped) {
            return NbtFactory.this.unwrapValue("", wrapped);
        }

        @Override
        public Object get(int index) {
            return this.wrapOutgoing(this.original.get(index));
        }

        @Override
        public int size() {
            return this.original.size();
        }

        @Override
        public Object set(int index, Object element) {
            return this.wrapOutgoing(this.original.set(index, this.unwrapIncoming(element)));
        }

        @Override
        public void add(int index, Object element) {
            Object nbt = this.unwrapIncoming(element);
            if (this.size() == 0) {
                NbtFactory.setFieldValue(NbtFactory.this.NBT_LIST_TYPE, this.handle, Byte.valueOf((byte)NbtFactory.access$1400((NbtFactory)NbtFactory.this, (Object)nbt).id));
            }
            this.original.add(index, nbt);
        }

        @Override
        public Object remove(int index) {
            return this.wrapOutgoing(this.original.remove(index));
        }

        @Override
        public boolean remove(Object o) {
            return this.original.remove(this.unwrapIncoming(o));
        }

        @Override
        public Object getHandle() {
            return this.handle;
        }
    }

    private class ConvertedMap
    extends AbstractMap<String, Object>
    implements Wrapper {
        private final Object handle;
        private final Map<String, Object> original;
        private final CachedNativeWrapper cache;

        public ConvertedMap(Object handle, Map<String, Object> original) {
            this.cache = new CachedNativeWrapper();
            this.handle = handle;
            this.original = original;
        }

        protected Object wrapOutgoing(Object value) {
            return this.cache.wrap(value);
        }

        protected Object unwrapIncoming(String key, Object wrapped) {
            return NbtFactory.this.unwrapValue(key, wrapped);
        }

        @Override
        public Object put(String key, Object value) {
            return this.wrapOutgoing(this.original.put(key, this.unwrapIncoming(key, value)));
        }

        @Override
        public Object get(Object key) {
            return this.wrapOutgoing(this.original.get(key));
        }

        @Override
        public Object remove(Object key) {
            return this.wrapOutgoing(this.original.remove(key));
        }

        @Override
        public boolean containsKey(Object key) {
            return this.original.containsKey(key);
        }

        @Override
        public Set<Map.Entry<String, Object>> entrySet() {
            return new AbstractSet<Map.Entry<String, Object>>(){

                @Override
                public boolean add(Map.Entry<String, Object> e) {
                    String key = e.getKey();
                    Object value = e.getValue();
                    ConvertedMap.this.original.put(key, ConvertedMap.this.unwrapIncoming(key, value));
                    return true;
                }

                @Override
                public int size() {
                    return ConvertedMap.this.original.size();
                }

                @Override
                public Iterator<Map.Entry<String, Object>> iterator() {
                    return ConvertedMap.this.iterator();
                }
            };
        }

        private Iterator<Map.Entry<String, Object>> iterator() {
            final Iterator<Map.Entry<String, Object>> proxy = this.original.entrySet().iterator();
            return new Iterator<Map.Entry<String, Object>>(){

                @Override
                public boolean hasNext() {
                    return proxy.hasNext();
                }

                @Override
                public Map.Entry<String, Object> next() {
                    Map.Entry entry = (Map.Entry)proxy.next();
                    return new AbstractMap.SimpleEntry<String, Object>((String)entry.getKey(), ConvertedMap.this.wrapOutgoing(entry.getValue()));
                }

                @Override
                public void remove() {
                    proxy.remove();
                }
            };
        }

        @Override
        public Object getHandle() {
            return this.handle;
        }

    }

    private final class CachedNativeWrapper {
        private final ConcurrentMap<Object, Object> cache;

        private CachedNativeWrapper() {
            this.cache = new MapMaker().weakKeys().makeMap();
        }

        public Object wrap(Object value) {
            Object current = this.cache.get(value);
            if (current == null && ((current = NbtFactory.this.wrapNative(value)) instanceof ConvertedMap || current instanceof ConvertedList)) {
                this.cache.put(value, current);
            }
            return current;
        }
    }

    public static interface Wrapper {
        public Object getHandle();
    }

    public final class NbtList
    extends ConvertedList {
        private NbtList(Object handle) {
            super(handle, NbtFactory.this.getDataList(handle));
        }
    }

    public final class NbtCompound
    extends ConvertedMap {
        private NbtCompound(Object handle) {
            super(handle, NbtFactory.this.getDataMap(handle));
        }

        public Byte getByte(String key, Byte defaultValue) {
            return this.containsKey(key) ? (Byte)this.get(key) : defaultValue;
        }

        public Short getShort(String key, Short defaultValue) {
            return this.containsKey(key) ? (Short)this.get(key) : defaultValue;
        }

        public Integer getInteger(String key, Integer defaultValue) {
            return this.containsKey(key) ? (Integer)this.get(key) : defaultValue;
        }

        public Long getLong(String key, Long defaultValue) {
            return this.containsKey(key) ? (Long)this.get(key) : defaultValue;
        }

        public Float getFloat(String key, Float defaultValue) {
            return this.containsKey(key) ? (Float)this.get(key) : defaultValue;
        }

        public Double getDouble(String key, Double defaultValue) {
            return this.containsKey(key) ? (Double)this.get(key) : defaultValue;
        }

        public String getString(String key, String defaultValue) {
            return this.containsKey(key) ? (String)this.get(key) : defaultValue;
        }

        public byte[] getByteArray(String key, byte[] defaultValue) {
            return this.containsKey(key) ? (byte[])this.get(key) : defaultValue;
        }

        public int[] getIntegerArray(String key, int[] defaultValue) {
            return this.containsKey(key) ? (int[])this.get(key) : defaultValue;
        }

        public NbtList getList(String key, boolean createNew) {
            NbtList list = (NbtList)this.get(key);
            if (list == null) {
                list = NbtFactory.createList(new Object[0]);
                this.put(key, list);
            }
            return list;
        }

        public NbtCompound getMap(String key, boolean createNew) {
            return this.getMap(Arrays.asList(key), createNew);
        }

        public NbtCompound putPath(String path, Object value) {
            List<String> entries = this.getPathElements(path);
            NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), true);
            map.put(entries.get(entries.size() - 1), value);
            return this;
        }

        public <T> T getPath(String path) {
            List<String> entries = this.getPathElements(path);
            NbtCompound map = this.getMap(entries.subList(0, entries.size() - 1), false);
            if (map != null) {
                return (T)map.get(entries.get(entries.size() - 1));
            }
            return null;
        }

        private NbtCompound getMap(Iterable<String> path, boolean createNew) {
            NbtCompound current = this;
            for (String entry : path) {
                NbtCompound child = (NbtCompound)current.get(entry);
                if (child == null) {
                    if (!createNew) {
                        throw new IllegalArgumentException("Cannot find " + entry + " in " + path);
                    }
                    child = NbtFactory.createCompound();
                    current.put(entry, child);
                }
                current = child;
            }
            return current;
        }

        private List<String> getPathElements(String path) {
            return Lists.newArrayList((Iterable)Splitter.on((String)".").omitEmptyStrings().split((CharSequence)path));
        }
    }

    private static enum NbtType {
        TAG_END(0, Void.class),
        TAG_BYTE(1, Byte.TYPE),
        TAG_SHORT(2, Short.TYPE),
        TAG_INT(3, Integer.TYPE),
        TAG_LONG(4, Long.TYPE),
        TAG_FLOAT(5, Float.TYPE),
        TAG_DOUBLE(6, Double.TYPE),
        TAG_BYTE_ARRAY(7, byte[].class),
        TAG_INT_ARRAY(11, int[].class),
        TAG_STRING(8, String.class),
        TAG_LIST(9, List.class),
        TAG_COMPOUND(10, Map.class);
        
        public final int id;

        private NbtType(int id, Class<?> type) {
            this.id = id;
            NBT_CLASS.put(id, type);
            NBT_ENUM.put(id, this);
        }

        private String getFieldName() {
            if (this == TAG_COMPOUND) {
                return "map";
            }
            if (this == TAG_LIST) {
                return "list";
            }
            return "data";
        }
    }

    public static enum StreamOptions {
        NO_COMPRESSION,
        GZIP_COMPRESSION;
        

        private StreamOptions() {
        }
    }

}