package org.powerstone.smartpagination.common;

import junit.framework.TestCase;

public class PageModelTest extends TestCase {
	private PageModel pm = null;

	protected void setUp() throws Exception {
		pm = new PageModel();
		pm.setTotalRecordsNumber(55);
		pm.setPageSize(10);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		pm = null;
		super.tearDown();
	}

	public void testDefaultComputeRecordsBeginNo() {
		super.assertEquals("default RecordsBeginNo",
				pm.computeRecordsBeginNo(), 0);
	}

	public void testToPageComputeRecordsBeginNo() {
		pm.setPageTo("4");
		super.assertEquals("default RecordsBeginNo",
				pm.computeRecordsBeginNo(), 30);
		pm.setPageTo("6");
		super.assertEquals("default RecordsBeginNo",
				pm.computeRecordsBeginNo(), 50);
	}

	public void testChangePageComputeRecordsBeginNo() {
		pm.setTotalRecordsNumber(55);
		pm.setPageSize(10);

		pm.setBeEnd(true);
		super.assertEquals("Change1", pm.computeRecordsBeginNo(), 50);
		pm.clear();

		pm.setBeFirst(true);
		super.assertEquals("Change2", pm.computeRecordsBeginNo(), 0);
		pm.clear();

		pm.setBeLast(true);
		super.assertEquals("Change3", pm.computeRecordsBeginNo(), 0);
		pm.clear();

		pm.setBeNext(true);
		super.assertEquals("Change4", pm.computeRecordsBeginNo(), 0);
		pm.clear();

		pm.setCurrPageNo("3");
		pm.setBeLast(true);
		super.assertEquals("Change5", pm.computeRecordsBeginNo(), 10);
		pm.clear();

		pm.setBeNext(true);
		super.assertEquals("Change6", pm.computeRecordsBeginNo(), 30);
		pm.clear();

	}

	public void testToPageSize20ComputeRecordsBeginNo() {
		pm.setPageTo("3");
		pm.setTotalRecordsNumber(88);
		pm.setPageSize(20);
		super.assertEquals("compute1", pm.computeRecordsBeginNo(), 40);

		pm.setPageSize(25);
		super.assertEquals("compute2", pm.computeRecordsBeginNo(), 50);
		pm.setPageTo("4");
		super.assertEquals("compute3", pm.computeRecordsBeginNo(), 75);
		pm.setPageTo("5");
		super.assertEquals("compute4", pm.computeRecordsBeginNo(), 75);

	}

	public void testDefaultComputeNewPageNoInTag() {
		super.assertEquals("default NewPageNo", pm.computeNewPageNo(), 1);
		pm.setPageTo("3");
		pm.setTotalRecordsNumber(88);
		pm.setPageSize(20);
		super.assertEquals("compute2", pm.computeNewPageNoInTag(), 3);
		pm.setPageTo("4");
		super.assertEquals("compute3", pm.computeNewPageNoInTag(), 4);
		pm.setPageTo("5");
		super.assertEquals("compute4", pm.computeNewPageNoInTag(), 5);
		pm.setPageTo("6");
		super.assertEquals("compute5", pm.computeNewPageNoInTag(), 5);
	}

	public void testDefaultComputePageCount() {
		super.assertEquals("default PageCount", pm.computePageCount(), 6);
		pm.setTotalRecordsNumber(88);
		pm.setPageSize(20);
		super.assertEquals("88/20", 5, pm.computePageCount());
	}

}