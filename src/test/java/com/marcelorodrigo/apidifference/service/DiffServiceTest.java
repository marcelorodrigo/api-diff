package com.marcelorodrigo.apidifference.service;

import com.marcelorodrigo.apidifference.exception.DiffException;
import com.marcelorodrigo.apidifference.exception.DiffNotFoundException;
import com.marcelorodrigo.apidifference.exception.InvalidBase64Exception;
import com.marcelorodrigo.apidifference.model.Diff;
import com.marcelorodrigo.apidifference.model.ResultType;
import com.marcelorodrigo.apidifference.repository.DiffRepository;
import com.marcelorodrigo.apidifference.vo.DiffResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
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

    @Test
    public void addLeftEmptyData() {
        assertThrows(InvalidBase64Exception.class, () -> diffService.addLeft("id-left-empty", ""));
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

    @Test
    public void addRightEmptyData() {
        assertThrows(InvalidBase64Exception.class, () -> diffService.addRight("id-right-empty", ""));
    }

    @Test
    public void validateDataMissingLeft() {
        assertThrows(DiffException.class, () -> diffService.validateDataPresence(new Diff("id").setRight("YQ==")));
    }

    @Test
    public void validateDataMissingRight() {
        assertThrows(DiffException.class, () -> diffService.validateDataPresence(new Diff("id").setLeft("Yg==")));
    }

    @Test
    public void getDifferenceNonExistingID() {
        final String id = "129";

        when(diffRepository.getById(id)).thenReturn(Optional.empty());
        assertThrows(DiffNotFoundException.class, () -> diffService.getDifference(id));
    }

    @Test
    public void getDifferenceEquals() throws Exception {
        final String id = "130";
        Diff diff = new Diff(id).setLeft("Yg==").setRight("Yg==");

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        DiffResult difference = diffService.getDifference(id);

        assertEquals(ResultType.EQUALS, difference.getResultType());
        assertEquals(ResultType.EQUALS.getMessage(), difference.getMessage());
    }

    @Test
    public void getDifferenceLengthNotEquals() throws Exception {
        final String id = "131";
        Diff diff = new Diff(id).setLeft("YjExYQ==").setRight("YmM=");

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        DiffResult difference = diffService.getDifference(id);

        assertEquals(ResultType.DIFFERENT_LENGTH, difference.getResultType());
        assertEquals(ResultType.DIFFERENT_LENGTH.getMessage(), difference.getMessage());
    }

    @Test
    public void getDifferenceSameLength() throws Exception {
        final String id = "132";
        Diff diff = new Diff(id).setLeft("Ym9ieQ==").setRight("YmFiYQ==");
        String messageExpected = "{offset=2, length=1}, {offset=4, length=1}";

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        DiffResult difference = diffService.getDifference(id);

        assertEquals(ResultType.SAME_LENGTH, difference.getResultType());
        assertEquals(messageExpected, messageExpected);
    }
}