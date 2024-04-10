package it.unimib.aiprint.source.implementations.image;

import static it.unimib.aiprint.util.Constants.NEWS_API_TEST_JSON_FILE;

import java.io.IOException;

import it.unimib.aiprint.model.image.ImageApiResponse;
import it.unimib.aiprint.util.JSONParserUtil;

public class ImageMockRemoteDataSource extends BaseImageRemoteDataSource {
    private final JSONParserUtil jsonParserUtil;

    public ImageMockRemoteDataSource(JSONParserUtil jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    @Override
    public void getAiImageUrl(String imageDescription) {
        ImageApiResponse imageApiResponse = null;
        try {
            imageApiResponse =
                    jsonParserUtil.parseJSONFileWithGSon(NEWS_API_TEST_JSON_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (imageApiResponse != null) {
            imageCallback.onSuccessFromRemote(imageApiResponse);
        } else {
            imageCallback.onFailureFromRemote(new Exception("API_KEY_ERROR"));
        };
    }
}
