package com.newapplol.request;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Created by Edwin on 23/12/2017.
 */

public class MatchRequest extends ApiRequest {

    private long matchId;

    public MatchRequest(RequestQueue queue, Context context) {
        super(queue, context);
    }


}
