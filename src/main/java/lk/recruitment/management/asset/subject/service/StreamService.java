package lk.recruitment.management.asset.subject.service;

import lk.recruitment.management.asset.subject.dao.StreamDao;
import lk.recruitment.management.asset.subject.entity.Stream;
import lk.recruitment.management.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// spring transactional annotation need to tell spring to this method work through the project
@CacheConfig(cacheNames = "stream")
public class StreamService implements AbstractService<Stream, Integer> {

    private final StreamDao streamDao;

    @Autowired
    public StreamService(StreamDao streamDao) {

        this.streamDao = streamDao;
    }

    @Cacheable
    public List<Stream> findAll() {

        return streamDao.findAll();
    }

    @Cacheable
    public Stream findById(Integer id) {
        return streamDao.getOne(id);
    }

    @Caching(evict = {@CacheEvict(value = "stream", allEntries = true)},
            put = {@CachePut(value = "stream", key = "#stream.id")})
    @Transactional
    public Stream persist(Stream stream) {
        return streamDao.save(stream);
    }

    @CacheEvict(allEntries = true)
    public boolean delete(Integer id) {
        streamDao.deleteById(id);
        return false;
    }

    @Cacheable
    public List<Stream> search(Stream stream) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Stream> streamExample = Example.of(stream, matcher);
        return streamDao.findAll(streamExample);
    }

    public boolean isStreamPresent(Stream stream) {
        return streamDao.equals(stream);
    }


/*    public List<Stream> findByProvince(Province province) {
        return streamDao.findByProvince(province);
    }*/
}
