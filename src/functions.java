class Functions 
{
    // rotate the image ***CLOCKWISE***
    private void rotateImage()
    {
        System.out.println("rotateImage called");
        for(int i=0; i<height; i++)
            for(int j=0; j<width; j++)
            {   
                int rgbArray[] = new int[4];
                
                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);
                
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            } 
        resetPicture();
    }
}