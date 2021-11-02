package org.powerstone.smartpagination.sample;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.powerstone.smartpagination.common.PageResult;
import org.powerstone.smartpagination.hibernate.HbmPageInfo;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BaseHibernateDao extends HibernateDaoSupport {

	public PageResult findByPage(HbmPageInfo pageInfo) {
		int recordsNumber = countRecordsNumber(pageInfo.getExpression(),
				pageInfo.getCountDistinctProjections());

		if (pageInfo.getPageSize() <= 0) {// PageSize==0相当于取全部
			List all = findByCriteria(pageInfo.getExpression());
			return new PageResult(all, recordsNumber, 1);// 取全部，所以页数为1
		}

		int pageAmount = (recordsNumber % pageInfo.getPageSize() > 0) ? (recordsNumber
				/ pageInfo.getPageSize() + 1)
				: (recordsNumber / pageInfo.getPageSize());
		int pageNo = pageInfo.getPageNo() > 0 ? pageInfo.getPageNo() : 1;

		if (pageNo > pageAmount) {
			pageNo = pageAmount;
		}

		if (pageInfo.getEnd() != null && pageInfo.getEnd().booleanValue()) {// end=true:首页
			pageNo = 1;
		}
		if (pageInfo.getEnd() != null && !pageInfo.getEnd().booleanValue()) {// end=false:尾页
			pageNo = pageAmount;
		}

		int firstResult = (pageNo - 1) * pageInfo.getPageSize();
		for (Order order : pageInfo.getOrderByList()) {
			pageInfo.getExpression().addOrder(order);
		}
		List pageData = findByCriteria(pageInfo.getExpression(), firstResult,
				pageInfo.getPageSize());

		return new PageResult(pageData, recordsNumber, pageAmount);
	}

	@SuppressWarnings("unchecked")
	public int countRecordsNumber(DetachedCriteria dc,
			String countDistinctProjections) {
		dc.setProjection(Projections.countDistinct(countDistinctProjections));
		List list = this.getHibernateTemplate().findByCriteria(dc);
		int result = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Integer item = (Integer) it.next();
			result += item;
		}
		dc.setProjection(null);// 避免对dc.setProjection影响到其它地方
		return result;
	}

	public <T> void delete(Class<T> entityClass, Serializable id) {
		getHibernateTemplate().delete(get(entityClass, id));
	}

	@SuppressWarnings("unchecked")
	public List findByCriteria(DetachedCriteria dc,
			int... firstResultAndMaxResults) {
		dc.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		if (firstResultAndMaxResults != null
				&& firstResultAndMaxResults.length == 2) {
			return this.getHibernateTemplate().findByCriteria(dc,
					firstResultAndMaxResults[0], firstResultAndMaxResults[1]);
		}

		return getHibernateTemplate().findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public <T> T load(Class<T> entityClass, Serializable id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	@SuppressWarnings("unchecked")
	public <T> T merge(T model) {
		return (T) getHibernateTemplate().merge(model);
	}

	public void saveOrUpdate(Object model) {
		getHibernateTemplate().saveOrUpdate(model);
	}

	public void flush() {
		getHibernateTemplate().flush();
	}
}