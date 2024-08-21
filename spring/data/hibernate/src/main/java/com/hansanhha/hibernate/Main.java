package com.hansanhha.hibernate;

import static java.lang.System.out;
import org.hibernate.cfg.Configuration;
import static org.hibernate.cfg.AvailableSettings.*;

public class Main {

    public static void main(String[] args) {
        var sessionFactory = new Configuration()
                .addAnnotatedClass(Book.class)
                // H2 인메모리 DB 사용
                .setProperty(URL, "jdbc:h2:mem:db1")
                .setProperty(USER, "sa")
                .setProperty(PASS, "")
                // Agroal 커넥션 풀 사용
                .setProperty("hibernate.agroal.maxSize", 20)
                // 콘솔에 SQL 출력
                .setProperty(SHOW_SQL, true)
                .setProperty(FORMAT_SQL, true)
                .setProperty(HIGHLIGHT_SQL, true)
                .buildSessionFactory();

        // 추론된 데이터베이스 스키마 내보내기
        sessionFactory.getSchemaManager().exportMappedObjects(true);

        // 엔티티 영속
        sessionFactory.inTransaction(session -> {
            session.persist(new Book("9781932394153", "Hibernate in Action"));
        });

        // HQL을 사용하여 데이터 쿼리
        sessionFactory.inSession(session -> {
            out.println(session.createSelectionQuery("select isbn|| ': '||title from Book").getSingleResult());
        });

        // criteria API를 사용하여 데이터 쿼리
        sessionFactory.inSession(session -> {
            var builder = sessionFactory.getCriteriaBuilder();
            var query = builder.createQuery(String.class);
            var book = query.from(Book.class);
            query.select(builder.concat(builder.concat(book.get(Book_.isbn), builder.literal(": ")),
                    book.get(Book_.title)));
            out.println(session.createSelectionQuery(query).getSingleResult());
        });
    }
}
