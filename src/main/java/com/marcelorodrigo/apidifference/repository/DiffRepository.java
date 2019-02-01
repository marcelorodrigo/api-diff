package com.marcelorodrigo.apidifference.repository;

import com.marcelorodrigo.apidifference.model.Diff;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class DiffRepository {

    public Map<String, Diff> repository = new HashMap();

    public Optional<Diff> getById(String id) {
        return Optional.ofNullable(repository.get(id));
    }

    public void save(Diff diff) {
        repository.put(diff.getId(), diff);
    }

}
