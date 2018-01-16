package com.nsn.quick4j.kit;

import java.util.*;

/**
 * 集合类工具类
 * 
 * @author donghao
 * @since 1.0
 */
public class CollectionKit {

	/**
	 * 判断collection是否为空
	 * @param coll 需判断的collection
	 * @return true：为空   false：非空
	 */
	public static boolean isEmpty(Collection<?> coll){
		return coll == null || coll.isEmpty();
	}
	
	/**
	 * 判断map是否为空
	 * @param map 需判断的map
	 * @return true：为空   false：非空
	 */
	public static boolean isEmpty(Map<?, ?> map){
		return map == null || map.isEmpty();
	}
	
	/**
	 * 判断collection是否非空
	 * @param coll 需判断的collection
	 * @return true:非空   false:为空
	 */
	public static boolean isNotEmpty(Collection<?> coll){
		return !isEmpty(coll);
	}

	/**
	 * 判断map是否非空
	 * @param map 需判断的map
	 * @return true:非空   false:为空
	 */
	public static boolean isNotEmpty(Map<?, ?> map){
		return !isEmpty(map);
	}

	/**
	 * 判断当前对象是否存在给定的list中
	 * @param list
	 * @param obj
     * @return
     */
	public static boolean isExist(List list, Object obj){
		boolean isExist = false;
		for(Object bean : list){
			if(bean.equals(obj)){
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	/**
	 * 将list中数据加入到set中，目的是实现去重
	 * @param set
	 * @param list
     * @return 若set为空返回null，否则加入后返回set
     */
	public static Set addListToSet(Set set,List list){
		if(set == null){
			return null;
		}
		for(Object obj : list){
			set.add(obj);
		}
		return set;
	}

	/**
	 * 将数组中数据加入到set中，去重
	 * @param set
	 * @param array
     * @return
     */
	public static Set addArrayToSet(Set set,Object[] array){
		if(set == null){
			return null;
		}
		for(Object obj : array){
			set.add(obj);
		}
		return set;
	}

	/**
	 * 将Set数据转换成list(ArrayList)
	 * @param set
	 * @return
     */
	public static List setToList(Set set){
		if(set == null){
			return null;
		}
		List list = new ArrayList();
		for(Object obj : set){
			list.add(obj);
		}
		return list;
	}

	/**
	 * 对list的进行排序，传入比较器
	 * @param list
	 * @param comparator
	 * @param <T>
     */
	public static<T> void sortList(List<T> list,Comparator<T> comparator){
		//排序
		Collections.sort(list,comparator);
	}

}
