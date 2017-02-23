package cn.ttsales.service;

import cn.ttsales.dao.TBookRepository;
import cn.ttsales.domain.TBook;
import cn.ttsales.util.PageInfo;
import cn.ttsales.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 露青 on 2016/10/20.
 */
@Service
public class BookSearchService {

    @Autowired
    private TBookRepository tBookRepository;

    public Page<TBook> search(final TBook book, PageInfo page) {
        return tBookRepository.findAll((root, query, cb) -> {
            if (null == book)
                return null;
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtil.isEmpty(book.getIsbn()))
                predicates.add(cb.equal(root.<String>get("isbn"), book.getIsbn()));
            if (!StringUtil.isEmpty(book.getName()))
                predicates.add(cb.like(root.<String>get("name"), "%" + book.getName() + "%"));
            if (!StringUtil.isEmpty(book.getPublishing()))
                predicates.add(cb.like(root.<String>get("publishing"), "%" + book.getPublishing() + "%"));
            if (!StringUtil.isEmpty(book.getAuthor()))
                predicates.add(cb.like(root.<String>get("author"), "%" + book.getAuthor() + "%"));
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        }, new PageRequest(page.getPage() - 1, page.getSize(), new Sort(Sort.Direction.DESC, page.getSortName())));
    }
}
