
//分享
new SocializeAction(this)
                .setPlatform(SocializePlatform.QQ)
                .withText("fuck")
                .withMedia(new ShareWeb("http://www.baidu.com"))
                .setCallback(new ShareListener() {
                    @Override
                    public void onStart(SocializePlatform share_media) {

                    }

                    @Override
                    public void onResult(SocializePlatform share_media) {

                    }

                    @Override
                    public void onError(SocializePlatform share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SocializePlatform share_media) {

                    }
                }).share();
//授权
        SocializeApp.get(this).getPlatformInfo(this, SocializePlatform.QQ, new AuthListener() {
            @Override
            public void onStart(SocializePlatform platform) {

            }

            @Override
            public void onComplete(SocializePlatform platform, int code, Map<String, String> data) {

            }

            @Override
            public void onError(SocializePlatform platform, int code, Throwable err) {

            }

            @Override
            public void onCancel(SocializePlatform platform, int code) {

            }
        });