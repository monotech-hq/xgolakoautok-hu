
(ns app.storage.frontend.dictionary)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOK {:by-alias-ascending
           {:en "By name (ascending)"
            :hu "Név szerint (növekvő)"}
           :by-alias-descending
           {:en "By name (descending)"
            :hu "Név szerint (csökkenő)"}
           :available-capacity-in-storage-is
           {:en "Available capacity in storage is: % MB"
            :hu "A rendelkezésre álló kapacitás a tárhelyen: % MB"}
           :cant-attach-more-files
           {:en "You can not select more files!"
            :hu "Nem választható ki több fájl!"}
           :failed-to-create-directory
           {:en "Failed to create directory"
            :hu "Sikertelen mappa létrehozás"}
           :file-preview
           {:en "Preview"
            :hu "Megtekintés"}
           :file-storage
           {:en "File storage"
            :hu "Tárhely"}
           :max-uploading-size-is
           {:en "Uploading size is: Max. % MB"
            :hu "Az egyszerre feltölthető fájlok mérete: max. % MB"}
           :my-storage
           {:en "My storage"
            :hu "Saját tárhely"}
           :new-directory
           {:en "New directory"
            :hu "Új mappa"}
           :new-file
           {:en "New file"
            :hu "Új fájl"}
           :search-in-storage
           {:en "Search in storage"
            :hu "Keresés a tárhelyen"}
           :storage
           {:en "Storage"
            :hu "Tárhely"}
           :unnamed-directory
           {:en "Unnamed directory"
            :hu "Névtelen mappa"}
           :unnamed-file
           {:en "Unnamed file"
            :hu "Névtelen fájl"}
           :uploading-size-is
           {:en "Uploading size is: %1 MB / %2 MB"
            :hu "A feltölteni kívánt fájlok mérete: %1 MB / %2 MB"}
           :will-be-deleted-after
           {:en ""
            :hu "A lomtárba helyezett elemek % nap után véglegesen törlődnek."}})
