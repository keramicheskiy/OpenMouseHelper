class BaseFunctions {


    fun String.getStringFromIndexToIndex(firstIndex: Int = 0, secondIndex: Int): String {
        var resultString = ""
        for (i in firstIndex..secondIndex) {
            resultString += this.get(i)
        }
        return resultString
    }



}