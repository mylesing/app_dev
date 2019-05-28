import QtQuick 2.7

Page1Form {
    property variant bwStrings: ["1", "2"]
    property variant count: 0
    function updatePage() {
        count = 0
        if (radioButton.checked) {
            bwStrings[count] = text4.text.toString()
            count = count + 1
        }
        if (radioButton1.checked) {
            bwStrings[count] = text3.text.toString()
            count = count + 1
        }
        if (radioButton2.checked) {
            bwStrings[count] = text2.text.toString()
            count = count + 1
        }
        if (radioButton3.checked) {
            bwStrings[count] = text1.text.toString()
            count = count + 1
        }
        if (radioButton4.checked) {
            bwStrings[count] = text4.text.toString()
            count = count + 1
        }
        if (radioButton5.checked) {
            bwStrings[count] = text3.text.toString()
            count = count + 1
        }
        if (radioButton6.checked) {
            bwStrings[count] = text2.text.toString()
            count = count + 1
        }
        if (radioButton7.checked) {
            bwStrings[count] = text1.text.toString()
            count = count + 1
        }

        if (count == 2) {
            radioButton.checked = true
            radioButton.checked = false
            radioButton7.checked = true
            radioButton7.checked = false

            console.log(bwStrings[0])
            console.log(bwStrings[1])
            console.log(text4.text)
            console.log(text4.text.toString())

            backend.bw = bwStrings;

            text4.text = backend.options[0]
            text3.text = backend.options[1]
            text2.text = backend.options[2]
            text1.text = backend.options[3]
        } else {
            print ("Pick both best and worst options.")
        }
    }

    submit.onClicked: updatePage()
}
