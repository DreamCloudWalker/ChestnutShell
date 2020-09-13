package com.dengjian.network.error;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class HttpError<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ErrorHandler.handleException(throwable));
    }
}
