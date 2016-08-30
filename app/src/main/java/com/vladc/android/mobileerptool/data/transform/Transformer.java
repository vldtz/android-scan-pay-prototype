package com.vladc.android.mobileerptool.data.transform;

import com.vladc.android.mobileerptool.data.db.entities.DataObject;
import com.vladc.android.mobileerptool.shared.service.BaseDto;

/**
 * Base class for transforming objects coming from REST API to entity objects  
 * @author VladA
 *
 * @param <I> type of object to be transformed from
 * @param <O> type of object to be transformed to
 */
public abstract class Transformer<I extends BaseDto<Long>, O extends DataObject<Long>> {

    public abstract O transform(final I input);

    /**
     * By default does nothing. Implement in extending classes if you need reverse transformation
     * @param input object to transform from
     * @param output object to transform to
     * @return transformed object
     */
    public I transformBack(final O input, final I output) {
        return output;
    }

    protected abstract O doTransform(final I input, final O output);
}
