package com.plm.mobilenet;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.InterpreterApi;
import org.tensorflow.lite.InterpreterFactoryApi;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.support.model.Model;
import org.tensorflow.lite.task.core.BaseOptions;
import org.tensorflow.lite.task.vision.classifier.Classifications;
import org.tensorflow.lite.task.vision.classifier.ImageClassifier;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelHelper {
    public void printInfo(Context context, String modelFileNameInAssets){
        Interpreter interpreter = loadModel(context, modelFileNameInAssets);
        Log.d("PABLO","input tensor count = "+interpreter.getInputTensorCount());

        // Obtener información del tensor de entrada
        int inputTensorCount = interpreter.getInputTensorCount();

        for (int i = 0; i < inputTensorCount; i++) {
            // Obtener el tensor de entrada
            Tensor inputTensor = interpreter.getInputTensor(i);

            // Obtener detalles del tensor
            int[] shape = inputTensor.shape(); // forma del tensor
            DataType dataType = inputTensor.dataType(); // tipo de datos del tensor

            // Mostrar información
            Log.d("PABLO","Input Tensor " + i + ": shape=" + Arrays.toString(shape) + ", dataType=" + dataType);
        }

        // Obtener información del tensor de salida
        int outputTensorCount = interpreter.getInputTensorCount();

        for (int i = 0; i < outputTensorCount; i++) {
            // Obtener el tensor de salida
            Tensor outputTensor = interpreter.getOutputTensor(i);

            // Obtener detalles del tensor
            int[] shape = outputTensor.shape(); // forma del tensor
            DataType dataType = outputTensor.dataType(); // tipo de datos del tensor

            // Mostrar información
            Log.d("PABLO","Output Tensor " + i + ": shape=" + Arrays.toString(shape) + ", dataType=" + dataType);
        }

        Log.d("PABLO","signature keys = "+interpreter.getSignatureKeys()!=null?Integer.toString(interpreter.getSignatureKeys().length):"null");
    }

    private Interpreter loadModel(Context context, String modelFileNameInAssets) {
        try {
            Interpreter.Options options = new Interpreter.Options();
            return new Interpreter(loadModelFile(context,modelFileNameInAssets), options);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el modelo de TensorFlow Lite", e);
        }
    }

    private MappedByteBuffer loadModelFile(Context context, String modelFileNameInAssets) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(modelFileNameInAssets);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public List<String[]> classify(Context context, File modelFile, Bitmap bitmap,List<String> labels) throws IOException{
        ImageClassifier imageClassifier=getModel(context,modelFile);
        TensorImage tensorImage=getTensorImage(bitmap,224,224, DataType.UINT8);

        // Clasificar la imagen
        List<Classifications> results = imageClassifier.classify(tensorImage);
        List<String[]> ret=new ArrayList<>();

        // Interpretar los resultados
        for (Classifications classification : results) {
            for (Category category : classification.getCategories()) {
                String labelText = labels.get(category.getIndex());
                float probability = category.getScore();
                ret.add(new String[]{labelText,Float.toString(probability)});
                // Aquí puedes usar labelText y probability como necesites
            }
        }

        return ret;
    }





    private TensorImage getTensorImage(Bitmap bitmap, int width, int height, DataType dataType){
        // Initialization code
        // Create an ImageProcessor with all ops required. For more ops, please
        // refer to the ImageProcessor Architecture section in this README.

        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(width, height, ResizeOp.ResizeMethod.BILINEAR))
                        .build();

        // Create a TensorImage object. This creates the tensor of the corresponding
        // tensor type (uint8 in this case) that the TensorFlow Lite interpreter needs.

        TensorImage tensorImage = new TensorImage(dataType);

        // Analysis code for every frame
        // Preprocess the image
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);

        return tensorImage;
    }

    private ImageClassifier getModel(Context context, File file) throws IOException {
        ImageClassifier imageClassifier = ImageClassifier.createFromFile(file);
        return imageClassifier;
    }


    public void detectObjects(Context context, File modelFile, Bitmap bitmap) throws IOException {
        //BaseOptions baseOptions = BaseOptions.builder().useGpu().build();

        ObjectDetector.ObjectDetectorOptions options =
                ObjectDetector.ObjectDetectorOptions.builder()
          //              .setBaseOptions(baseOptions)
                        .setMaxResults(5)
                        .setScoreThreshold(0.5f)
                        .build();

        // Crear ObjectDetector con las opciones configuradas.
        ObjectDetector objectDetector =
                ObjectDetector.createFromFileAndOptions(modelFile, options);

        TensorImage tensorImage=getTensorImage(bitmap,320,320, DataType.INT32);
        List<Detection> resultados = objectDetector.detect(tensorImage);
    }


}


/*
// Activar la delegación de GPU.
BaseOptions baseOptions = BaseOptions.builder().useGpu().build();
ObjectDetectorOptions options =
    ObjectDetectorOptions.builder()
        .setBaseOptions(baseOptions)
        .setMaxResults(1)
        .build();

// Crear ObjectDetector con las opciones configuradas.
ObjectDetector objectDetector =
    ObjectDetector.createFromFileAndOptions(context, modelo, options);

// Realizar la detección
List<Detection> resultados = objectDetector.detect(imagen);

 */