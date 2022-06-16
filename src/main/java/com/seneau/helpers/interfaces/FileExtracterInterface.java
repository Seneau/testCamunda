package com.seneau.helpers.interfaces;

import com.seneau.domain.CongeData;

import java.io.InputStream;
import java.util.List;

public interface FileExtracterInterface {
    List<CongeData>  congeDataFiletoCongeData(InputStream is);
}
