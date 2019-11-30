package com.edi3.dao.impl.business_processes;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.business_processes.ExecutorTask;
import com.edi3.core.business_processes.ExecutorTaskFolderStructure;
import com.edi3.core.categories.User;
import com.edi3.core.enumerations.FolderStructure;
import com.edi3.core.enumerations.ProcessType;
import com.edi3.dao.i.business_processes.ExecutorTaskFolderStructureDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
public class ExecutorTaskFolderStructureDaoImpl implements ExecutorTaskFolderStructureDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void save(ExecutorTaskFolderStructure executorTaskFolderStructure) {
        sessionFactory.getCurrentSession().save(executorTaskFolderStructure);
    }

    public void update(ExecutorTaskFolderStructure executorTaskFolderStructure) {
        sessionFactory.getCurrentSession().update(executorTaskFolderStructure);
    }

    public void delete(ExecutorTaskFolderStructure executorTaskFolderStructure) {
        sessionFactory.getCurrentSession().delete(executorTaskFolderStructure);
    }

    public List<ExecutorTaskFolderStructure> getExecutorList(User user, FolderStructure folderStructure, String filterString, String groupBy, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {

        System.out.println("ExecutorTaskFolderStructureDaoImpl - getExecutorList() begin");
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTaskFolderStructure> cq = cb.createQuery(ExecutorTaskFolderStructure.class);
        Root<ExecutorTaskFolderStructure> executorTaskFolderStructureRoot = cq.from(ExecutorTaskFolderStructure.class);
        Join<ExecutorTaskFolderStructure, ExecutorTask> executorTaskJoin = executorTaskFolderStructureRoot.join("executorTask", JoinType.LEFT);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = executorTaskJoin.join("document", JoinType.LEFT);

        // cb.and() - always true, cb.or() - always false
        cq.where(cb.and(
                cb.equal(executorTaskFolderStructureRoot.get("user"), user),
                cb.equal(executorTaskFolderStructureRoot.get("folder"), folderStructure),
                (Objects.isNull(abstractDocumentEdiClass)) ?
                        (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                                cb.or(
                                        cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                        cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, executorTaskJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"))) :
                        cb.and(
                                cb.equal(abstractDocumentJoin.type(), abstractDocumentEdiClass),
                                ("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                                        cb.or(
                                                ( (folderStructure == FolderStructure.INBOX || folderStructure == FolderStructure.TRASH)&&("author".equals(groupBy)) ? cb.like(cb.lower(abstractDocumentJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%") :
                                                        (folderStructure == FolderStructure.INBOX || folderStructure == FolderStructure.TRASH)&&("sender".equals(groupBy)) ? cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%") :
                                                                (folderStructure == FolderStructure.SENT || folderStructure == FolderStructure.DRAFT)&&("author".equals(groupBy)) ? cb.like(cb.lower(abstractDocumentJoin.get("whomString")), "%" + filterString.toLowerCase() + "%") :
                                                                        (folderStructure == FolderStructure.SENT || folderStructure == FolderStructure.DRAFT)&&("sender".equals(groupBy)) ? cb.like(cb.lower(executorTaskJoin.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%") :
                                                                                (folderStructure == FolderStructure.MARKED)&&("sender".equals(groupBy)) ?
                                                                                        cb.or(cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                                                                                cb.like(cb.lower(executorTaskJoin.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%")  ) :
                                                                                        (folderStructure == FolderStructure.MARKED)&&("author".equals(groupBy)) ?
                                                                                                cb.or(cb.like(cb.lower(abstractDocumentJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                                                                                        cb.like(cb.lower(abstractDocumentJoin.get("whomString")), "%" + filterString.toLowerCase() + "%")  ) :
                                                                                                cb.or()
                                                ),
                                                (Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).count() == 0 ? cb.or() :
                                                        executorTaskJoin.get("processType").in(Arrays.stream(ProcessType.values()).filter(n -> n.getRuName().toLowerCase().contains(filterString.toLowerCase())).collect(Collectors.toList()))),
                                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%"),
                                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, executorTaskJoin.get("date"), cb.literal("'%d.%m.%Y %k %i'"))), "%" + filterString.toLowerCase() + "%")
                                        )
                        )
                )
        );

        cq.select(executorTaskFolderStructureRoot);
        System.out.println("ExecutorTaskFolderStructureDaoImpl - getExecutorList() before createQuery");
        TypedQuery<ExecutorTaskFolderStructure> q = session.createQuery(cq);
        System.out.println("ExecutorTaskFolderStructureDaoImpl - getExecutorList() after createQuery");
        return q.getResultList();
    }

    public List<ExecutorTaskFolderStructure> getCommonList(User user, String filterString, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ExecutorTaskFolderStructure> cq = cb.createQuery(ExecutorTaskFolderStructure.class);
        Root<ExecutorTaskFolderStructure> executorTaskFolderStructureRoot = cq.from(ExecutorTaskFolderStructure.class);
        Join<ExecutorTaskFolderStructure, ExecutorTask> executorTaskJoin = executorTaskFolderStructureRoot.join("executorTask", JoinType.LEFT);
        Join<ExecutorTask, AbstractDocumentEdi> abstractDocumentJoin = executorTaskJoin.join("document", JoinType.LEFT);

        cq.where(cb.and(
                cb.equal(executorTaskFolderStructureRoot.get("user"), user),
                cb.notEqual(executorTaskFolderStructureRoot.get("folder"), FolderStructure.DRAFT),
                cb.equal(abstractDocumentJoin.type(), abstractDocumentEdiClass),
                (("".equals(filterString) || Objects.isNull(filterString)) ? cb.and() :
                        cb.or(
                                cb.like(cb.lower(abstractDocumentJoin.get("number")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, executorTaskJoin.get("date"), cb.literal("'%d.%m.%Y %T'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(cb.function("DATE_FORMAT", String.class, abstractDocumentJoin.get("date"), cb.literal("'%d.%m.%Y'"))), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(executorTaskJoin.get("author").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(executorTaskJoin.get("executor").get("fio")), "%" + filterString.toLowerCase() + "%"),
                                cb.like(cb.lower(abstractDocumentJoin.get("theme")), "%" + filterString.toLowerCase() + "%")
                        )
                )
        ));

        cq.select(executorTaskFolderStructureRoot);
        TypedQuery<ExecutorTaskFolderStructure> q = session.createQuery(cq);
        return q.getResultList();
    }

    public List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUser(User user, ExecutorTask executorTask) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTaskFolderStructure> query = session.createQuery(
                "select executorTaskFolderStructure " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and executorTaskFolderStructure.executorTask = :executorTask ", ExecutorTaskFolderStructure.class);

        query.setParameter("user", user);
        query.setParameter("executorTask", executorTask);

        return query.getResultList();
    }

    public List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUserDocumentProcessType(User user, AbstractDocumentEdi documentEdi, ProcessType processType) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTaskFolderStructure> query = session.createQuery(
                "select executorTaskFolderStructure " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and executorTask.processType = :processType " +
                        "and abstractdocument = :documentEdi ", ExecutorTaskFolderStructure.class);

        query.setParameter("user", user);
        query.setParameter("documentEdi", documentEdi);
        query.setParameter("processType", processType);

        return query.getResultList();
    }

    public List<ExecutorTaskFolderStructure> getExecutorTaskFolderStructureByUserDocument(User user, AbstractDocumentEdi documentEdi) {
        Session session = sessionFactory.getCurrentSession();

        Query<ExecutorTaskFolderStructure> query = session.createQuery(
                "select executorTaskFolderStructure " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and abstractdocument = :documentEdi ", ExecutorTaskFolderStructure.class);

        query.setParameter("user", user);
        query.setParameter("documentEdi", documentEdi);

        return query.list();
    }

    public HashMap<FolderStructure, Integer> getTaskCountByFolders(User user, Class<? extends AbstractDocumentEdi> abstractDocumentEdiClass) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(
                "select new map(executorTaskFolderStructure.folder, count( distinct abstractdocument.id)) " +
                        "from ExecutorTaskFolderStructure executorTaskFolderStructure " +
                        "left join ExecutorTask as executorTask on executorTaskFolderStructure.executorTask.id = executorTask.id " +
                        "and (executorTask.author = :user or executorTask.executor = :user) " +
                        "left join AbstractDocumentEdi as abstractdocument on abstractdocument.id = executorTask.document.id " +
                        "where executorTaskFolderStructure.user = :user " +
                        "and executorTask.result is null " +
                        "and type(abstractdocument)= :abstractDocumentEdiClass " +
                        "group by executorTaskFolderStructure.folder"
        );// Set correct type of Query ?

        query.setParameter("user", user);
        query.setParameter("abstractDocumentEdiClass", abstractDocumentEdiClass);

        @SuppressWarnings("unchecked") List<HashMap<String, Object>> list = query.list(); // This type because you can't get everything from the query

        HashMap<FolderStructure, Integer> mapFolderStructure
                = list.stream().filter(t -> Objects.nonNull(t.get("0")) && Objects.nonNull(t.get("1"))).collect(HashMap::new,
                (m, v) ->
                        m.put((FolderStructure) v.get("0"), ((Long) v.get("1")).intValue()),
                HashMap::putAll);

        return mapFolderStructure;
    }
}
