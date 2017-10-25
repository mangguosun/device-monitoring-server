package cn.dunn.im.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.dunn.im.dao.BaseDao;

public abstract class BaseService<PK extends Serializable, T> {

	protected abstract BaseDao<PK, T> getEntityDao();

	public void save(T t) {
		getEntityDao().save(t);
	}

	public void delete(PK pk) {
		getEntityDao().delete(pk);
	}

	public void update(T t) {
		getEntityDao().update(t);
	}

	public T getById(PK pk) {
		return getEntityDao().getById(pk);
	}

	public List<T> getAll(Map<String, Object> condition) {
		return getEntityDao().getAll(condition);
	}

	public T getUnique(String sql, Map<String, Object> map) {
		return getEntityDao().getUnique(sql, map);
	}

	public List<T> getList(String sql, Map<String, Object> map) {
		return getEntityDao().getList(sql, map);
	}

	public <C> List<C> getList(String sql, Map<String, Object> map, Class<C> clazz) {
		return getEntityDao().getList(sql, map, clazz);
	}

	public int getCount(String sql, Map<String, Object> map) {
		return getEntityDao().getCount(sql, map);
	}
}
