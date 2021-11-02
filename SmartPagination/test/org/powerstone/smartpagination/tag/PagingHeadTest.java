package org.powerstone.smartpagination.tag;

import javax.servlet.jsp.PageContext;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mockito.Mockito;
import org.powerstone.smartpagination.common.PageModel;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class PagingHeadTest extends TestCase {

	private static final Log logger = LogFactory.getLog(PagingHeadTest.class);

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGenHtml() {
		PagingHead ph = new PagingHead();
		ph.setOrderBy("PPP1");
		ph.setUrl("/home.do");
		ph.setStyleClass("styleClass");
		PageModel pm = new PageModel();
		pm.setOrderBy("xxxxx");
		pm.setPageSize(10);

		PageContext pageContext = Mockito.mock(PageContext.class);
		Mockito.stub(pageContext.getRequest()).toReturn(new MockHttpServletRequest());
		Mockito.stub(pageContext.getResponse()).toReturn(new MockHttpServletResponse());
		ph.setPageContext(pageContext);

		String result = ph.genHtml(pm, "/smart");
		logger.debug(result);
		Assert.assertTrue(result.indexOf("PPP1") > 0);
		Assert.assertTrue(result.indexOf("-up") < 0);

		pm = new PageModel();
		pm.setOrderBy("PPP1");
		ph.setStyleClass("styleClass");
		pm.setPageSize(10);
		result = ph.genHtml(pm, "/smart");
		logger.debug(result);
		Assert.assertTrue(result.indexOf("PPP1") > 0);
		Assert.assertTrue(result.indexOf("-up") < 0);

		ph = new PagingHead();
		ph.setOrderBy("PPP1");
		ph.setUrl("/home.do");
		ph.setStyleClass("styleClass");
		pm = new PageModel();
		pm.setOrderBy("PPP1");
		pm.setOrderDirection("desc");
		pm.setPageSize(10);

		pageContext = Mockito.mock(PageContext.class);
		Mockito.stub(pageContext.getRequest()).toReturn(new MockHttpServletRequest());
		Mockito.stub(pageContext.getResponse()).toReturn(new MockHttpServletResponse());
		ph.setPageContext(pageContext);

		result = ph.genHtml(pm, "/smart");

		logger.debug(result);
		Assert.assertTrue(result.indexOf("PPP1") > 0);
		Assert.assertTrue(result.indexOf("-down") > 0);

		String s1 = result.substring(0, result.indexOf("</label>"));
		Assert.assertTrue(s1.endsWith("id=\"coltitle\">"));
		String s2 = result.substring(result.indexOf("</label>"));
		Assert.assertTrue(result.equals(s1 + s2));
		logger.debug(s1 + "ccccc" + s2);
	}
}
