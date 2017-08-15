

public class BookImpl implements IBookDao{
	
	private IBaseDao baseDao;
	
	public ListPage find(int pageNo) {
		return baseDao.find(Book.class, pageNo);
	}

	public void delete(Integer id) {
		baseDao.delete(Book.class, id);
	}
	
	public void save(Object object) {
		baseDao.save(object);
	}

}




