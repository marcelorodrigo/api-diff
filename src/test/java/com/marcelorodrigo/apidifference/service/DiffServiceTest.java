package com.marcelorodrigo.apidifference.service;

import com.marcelorodrigo.apidifference.exception.DiffException;
import com.marcelorodrigo.apidifference.exception.DiffNotFoundException;
import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import com.marcelorodrigo.apidifference.model.Diff;
import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.repository.DiffRepository;
import com.marcelorodrigo.apidifference.vo.DiffResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiffServiceTest {

    @Mock
    private DiffRepository diffRepository;

    @InjectMocks
    private DiffService diffService;

    @Test
    public void getByIDExisting() {
        // Arrange
        String id = "412";
        Diff shouldReturn = new Diff(id);

        // Act
        when(diffRepository.getById(id)).thenReturn(Optional.of(shouldReturn));
        // Assert
        assertEquals(Optional.of(shouldReturn), diffService.getById(id));
    }

    @Test
    public void addLeftNewRecord() throws Exception {
        String id = "121";
        String encoded = "VHVlc2RheQ==";
        String decoded = "Tuesday";

        Diff saved = diffService.addLeft(id, encoded);

        assertEquals(id, saved.getId());
        assertEquals(decoded, saved.getLeft());
        assertNull(saved.getRight());
    }

    @Test(expected = InvalidBase64Exception.class)
    public void addLeftEmptyData() throws Exception {
        diffService.addLeft("id-left-empty", "");
    }

    @Test
    public void addRightNewRecord() throws Exception {
        String id = "123";
        String encoded = "TW9uZGF5";
        String decoded = "Monday";

        Diff saved = diffService.addRight(id, encoded);

        assertEquals(id, saved.getId());
        assertEquals(decoded, saved.getRight());
        assertNull(saved.getLeft());
    }

    @Test(expected = InvalidBase64Exception.class)
    public void addRightEmptyData() throws Exception {
        diffService.addRight("id-right-empty", "");
    }

    @Test(expected = DiffException.class)
    public void validateDataMissingLeft() throws Exception {
        diffService.validateDataPresence(new Diff("id").setRight("YQ=="));
    }

    @Test(expected = DiffException.class)
    public void validateDataMissingRight() throws Exception {
        diffService.validateDataPresence(new Diff("id").setLeft("Yg=="));
    }

    @Test(expected = DiffNotFoundException.class)
    public void getDifferenceNonExistingID() throws Exception {
        String id = "129";

        when(diffRepository.getById(id)).thenReturn(Optional.empty());
        diffService.getDifference(id);
    }

    @Test
    public void getDifferenceEquals() throws Exception {
        String id = "130";
        Diff diff = new Diff(id).setLeft("Yg==").setRight("Yg==");

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        DiffResult difference = diffService.getDifference(id);

        assertEquals(ResultType.EQUALS, difference.getResultType());
        assertEquals(ResultType.EQUALS.getMessage(), difference.getMessage());
    }

    @Test
    public void getDifferenceLengthNotEquals() throws Exception {
        String id = "131";
        Diff diff = new Diff(id).setLeft("YjExYQ==").setRight("YmM=");

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        DiffResult difference = diffService.getDifference(id);

        assertEquals(ResultType.DIFFERENT_LENGTH, difference.getResultType());
        assertEquals(ResultType.DIFFERENT_LENGTH.getMessage(), difference.getMessage());
    }

    @Test
    public void getDifferenceSameLength() throws Exception {
        String id = "132";
        Diff diff = new Diff(id).setLeft("Ym9ieQ==").setRight("YmFiYQ==");
        String messageExpected = "{offset=2, length=1}, {offset=4, length=1}";

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        DiffResult difference = diffService.getDifference(id);

        assertEquals(ResultType.SAME_LENGTH, difference.getResultType());
        assertEquals(messageExpected, messageExpected);
    }
}