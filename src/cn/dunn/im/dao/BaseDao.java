package cn.dunn.im.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDao<PK extends Serializable, T> extends SqlSessionDaoSupport {
	protected abstract Class<T> getEntity();

	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	public void save(T t) {
		getSqlSession().insert(getEntity().getName() + ".save", t);
	}

	public void delete(PK pk) {
		getSqlSession().delete(getEntity().getName() + ".delete", pk);
	}

	public void update(T t) {
		getSqlSession().update(getEntity().getName() + ".update", t);
	}

	public void update(String sql, Map<String, Object> map) {
		getSqlSession().update(getEntity().getName() + "." + sql, map);
	}

	public T getById(PK pk) {
		return getSqlSession().selectOne(getEntity().getName() + ".getById", pk);
	}

	public List<T> getAll(Map<String, Object> condition) {
		return getSqlSession().selectList(getEntity().getName() + ".getAll", condition);
	}

	public T getUnique(String sql, Map<String, Object> map) {
		List<T> list = getSqlSession().selectList(getEntity().getName() + "." + sql, map);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public List<T> getList(String sql, Map<String, Object> map) {
		return getSqlSession().selectList(getEntity().getName() + "." + sql, map);
	}

	public <C> List<C> getList(String sql, Map<String, Object> map, Class<C> clazz) {
		return getSqlSession().selectList(getEntity().getName() + "." + sql, map);
	}

	public int getCount(String sql, Map<String, Object> map) {
		return getSqlSession().selectOne(getEntity().getName() + "." + sql, map);
	}
}
