package hibernate;

import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Book.class)
public abstract class Book_ {

	public static final String ISBN = "isbn";
	public static final String TITLE = "title";

	
	/**
	 * @see hibernate.Book#isbn
	 **/
	public static volatile SingularAttribute<Book, String> isbn;
	
	/**
	 * @see hibernate.Book#title
	 **/
	public static volatile SingularAttribute<Book, String> title;
	
	/**
	 * @see hibernate.Book
	 **/
	public static volatile EntityType<Book> class_;

}

