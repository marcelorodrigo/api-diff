package com.marcelorodrigo.apidifference.service;

import com.marcelorodrigo.apidifference.exception.DiffException;
import com.marcelorodrigo.apidifference.exception.DiffNotFoundException;
import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import com.marcelorodrigo.apidifference.model.Diff;
import com.marcelorodrigo.apidifference.model.Difference;
import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.repository.DiffRepository;
import com.marcelorodrigo.apidifference.util.Base64Util;
import com.marcelorodrigo.apidifference.util.StringProcessor;
import com.marcelorodrigo.apidifference.vo.DiffResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiffService {

    public static final String MISSING_LEFT_DATA = "Missing left data to compare differences";
    public static final String MISSING_RIGHT_DATA = "Missing right data to compare differences";

    private final DiffRepository diffRepository;

    public DiffService(DiffRepository diffRepository) {
        this.diffRepository = diffRepository;
    }

    public Optional<Diff> getById(String id) {
        return diffRepository.getById(id);
    }

    public Diff addLeft(final String id, final String data) throws InvalidBase64Exception {
        if (ObjectUtils.isEmpty(data)) {
            throw new InvalidBase64Exception("Data is empty");
        }
        var diff = getById(id).orElse(new Diff(id));
        diff.setLeft(Base64Util.decode(data));
        diffRepository.save(diff);
        return diff;
    }


    public Diff addRight(final String id, final String data) throws InvalidBase64Exception {
        if (ObjectUtils.isEmpty(data)) {
            throw new InvalidBase64Exception("Data is empty");
        }
        var diff = getById(id).orElse(new Diff(id));
        diff.setRight(Base64Util.decode(data));
        diffRepository.save(diff);
        return diff;
    }

    /**
     * Get Diffresult from a recorded Diff
     *
     * @param id Diff ID
     * @return DiffResult
     * @throws DiffNotFoundException When a Diff not found by provided ID
     * @throws DiffException         When Diff entity has invalid or missing data
     */
    public DiffResult getDifference(String id) throws DiffNotFoundException, DiffException {
        var diff = getById(id).orElseThrow(DiffNotFoundException::new);
        validateDataPresence(diff);

        return getDiffFromStrings(diff.getLeft(), diff.getRight());
    }

    private DiffResult getDiffFromStrings(String left, String right) {
        var result = new DiffResult();

        if (left.equals(right)) {
            result.setResultType(ResultType.EQUALS);
        } else if (left.length() != right.length()) {
            result.setResultType(ResultType.DIFFERENT_LENGTH);
        } else {
            var collect = StringProcessor.getDifferences(left, right)
                    .stream().map(Difference::toString)
                    .collect(Collectors.joining(", "));
            result.setResultType(ResultType.SAME_LENGTH);
            result.setMessage(collect);
        }
        return result;
    }

    /**
     * Validate presence of data on left and right sides
     *
     * @param diff Diff entity
     * @throws DiffException Throws if one of sides is missing or empty
     */
    public void validateDataPresence(Diff diff) throws DiffException {
        if (ObjectUtils.isEmpty(diff.getLeft())) {
            throw new DiffException(MISSING_LEFT_DATA);
        }
        if (ObjectUtils.isEmpty(diff.getRight())) {
            throw new DiffException(MISSING_RIGHT_DATA);
        }
    }

}
