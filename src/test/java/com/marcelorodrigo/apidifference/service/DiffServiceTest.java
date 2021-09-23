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
class DiffServiceTest {

    @Mock
    private DiffRepository diffRepository;

    @InjectMocks
    private DiffService diffService;

    @Test
    void getByIDExisting() {
        // Arrange
        var id = "412";
        var shouldReturn = new Diff(id);

        // Act
        when(diffRepository.getById(id)).thenReturn(Optional.of(shouldReturn));
        // Assert
        assertEquals(Optional.of(shouldReturn), diffService.getById(id));
    }

    @Test
    void addLeftNewRecord() throws Exception {
        var id = "121";
        var encoded = "VHVlc2RheQ==";
        var decoded = "Tuesday";

        var saved = diffService.addLeft(id, encoded);

        assertEquals(id, saved.getId());
        assertEquals(decoded, saved.getLeft());
        assertNull(saved.getRight());
    }

    @Test
    void addLeftEmptyData() {
        assertThrows(InvalidBase64Exception.class, () -> diffService.addLeft("id-left-empty", ""));
    }

    @Test
    void addRightNewRecord() throws Exception {
        var id = "123";
        var encoded = "TW9uZGF5";
        var decoded = "Monday";

        var saved = diffService.addRight(id, encoded);

        assertEquals(id, saved.getId());
        assertEquals(decoded, saved.getRight());
        assertNull(saved.getLeft());
    }

    @Test
    void addRightEmptyData() {
        assertThrows(InvalidBase64Exception.class, () -> diffService.addRight("id-right-empty", ""));
    }

    @Test
    void validateDataMissingLeft() {
        assertThrows(DiffException.class, () -> diffService.validateDataPresence(new Diff("id").setRight("YQ==")));
    }

    @Test
    void validateDataMissingRight() {
        assertThrows(DiffException.class, () -> diffService.validateDataPresence(new Diff("id").setLeft("Yg==")));
    }

    @Test
    void getDifferenceNonExistingID() {
        final var id = "129";

        when(diffRepository.getById(id)).thenReturn(Optional.empty());
        assertThrows(DiffNotFoundException.class, () -> diffService.getDifference(id));
    }

    @Test
    void getDifferenceEquals() throws Exception {
        final var id = "130";
        var diff = new Diff(id).setLeft("Yg==").setRight("Yg==");

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        var difference = diffService.getDifference(id);

        assertEquals(ResultType.EQUALS, difference.getResultType());
        assertEquals(ResultType.EQUALS.getMessage(), difference.getMessage());
    }

    @Test
    void getDifferenceLengthNotEquals() throws Exception {
        final var id = "131";
        var diff = new Diff(id).setLeft("YjExYQ==").setRight("YmM=");

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        var difference = diffService.getDifference(id);

        assertEquals(ResultType.DIFFERENT_LENGTH, difference.getResultType());
        assertEquals(ResultType.DIFFERENT_LENGTH.getMessage(), difference.getMessage());
    }

    @Test
    void getDifferenceSameLength() throws Exception {
        final var id = "132";
        final var diff = new Diff(id).setLeft("Ym9ieQ==").setRight("YmFiYQ==");
        var messageExpected = "{offset=2, length=1}, {offset=4, length=1}";

        when(diffRepository.getById(id)).thenReturn(Optional.of(diff));
        var difference = diffService.getDifference(id);

        assertEquals(ResultType.SAME_LENGTH, difference.getResultType());
        assertEquals(messageExpected, difference.getMessage());
    }
}