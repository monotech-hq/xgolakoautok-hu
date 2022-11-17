
(ns app.storage.backend.media-browser.mutations
    (:require [app.common.backend.api                       :as common]
              [app.storage.backend.core.helpers             :as core.helpers]
              [app.storage.backend.media-browser.config     :as media-browser.config]
              [app.storage.backend.media-browser.prototypes :as media-browser.prototypes]
              [app.storage.backend.core.side-effects        :as core.side-effects]
              [candy.api                                    :refer [return]]
              [com.wsscode.pathom3.connect.operation        :as pathom.co :refer [defmutation]]
              [engines.item-browser.api                     :as item-browser]
              [io.api                                       :as io]
              [map.api                                      :as map]
              [mongo-db.api                                 :as mongo-db]
              [pathom.api                                   :as pathom]
              [time.api                                     :as time]
              [vector.api                                   :as vector]
              [x.media.api                                  :as x.media]))

;; -- Remove media-item links -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn remove-item-links!
  ; @param (string) item-id
  ;
  ; @return (?)
  [document item-id]
  (let [{:media/keys [filename]} (mongo-db/get-document-by-id "storage" item-id)
        media-storage-uri        (x.media/filename->media-storage-uri filename)]
       (letfn [(f [%] (= % media-storage-uri))]
              (map/->>remove-values-by document f))))

(defn clean-collection!
  ; @param (string) collection-name
  ; @param (string) item-id
  ;
  ; @return (?)
  [collection-name item-id]
  (mongo-db/apply-documents! collection-name #(remove-item-links! % item-id) {}))

(defn clean-collections!
  ; @param (string) item-id
  ;
  ; @return (?)
  [item-id]
  ; XXX#6781 (app.common.frontend.item-selector.events)
  ; A kitörölt media elemek hivatkozásait szükséges eltávolítani az adatbázis más kollekcióiból,
  ; mivel a media-selector kizárólag olyan elemek kiválasztását tudja megszüntetni, amelyek elérhető
  ; dokumentummal rendelkeznek!
  (let [collection-names (mongo-db/get-collection-names)]
       (letfn [(f [result collection-name] (conj result (clean-collection! collection-name item-id)))]
              (reduce f [] (vector/remove-item collection-names "storage")))))

;; -- Permanently delete item(s) functions ------------------------------------
;; ----------------------------------------------------------------------------

; 1. Törli az elemek hivatkozásait, levonja az elemek méretét a felmenő mappák
;    {:size ...} tulajdonságából, és frissíti a felmenő mappákat.
; 2. x mp elteltével, ha NEM történt meg az elemek visszaállítása, akkor véglegesen
;    törli az elemeket és azok leszármazott elemeit, illetve törli a fájlokat és bélyegképeket.

(defn delete-file-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)
  ;   :parent-id (string)}
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [{:media/keys [filename] :as file-item} (core.side-effects/get-item env item-id)]
            (core.side-effects/remove-item! env file-item)
            (core.side-effects/delete-file! filename)))

(defn delete-directory-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)
  ;   :parent-id (string)}
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [directory-item (core.side-effects/get-item env item-id)]
            (core.side-effects/remove-item! env directory-item)
            (let [items (get directory-item :media/items)]
                 (doseq [{:media/keys [id]} items]
                        (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env id)]
                                (let [mutation-props {:item-id id :parent-id item-id}]
                                     (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                                                         (delete-file-f      env mutation-props))))))))

(defn delete-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)
  ;   :parent-id (string)}
  [env {:keys [item-id parent-id] :as mutation-props}]
  (if-not (core.side-effects/item-attached? env parent-id {:media/id item-id})
          (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env item-id)]
                  (case mime-type "storage/directory" (delete-directory-f env mutation-props)
                                                      (delete-file-f      env mutation-props)))))

;; -- Temporary delete item(s) mutations --------------------------------------
;; ----------------------------------------------------------------------------

(defn delete-item-temporary-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)
  ;   :parent-id (string)}
  ;
  ; @return (string)
  [env {:keys [item-id parent-id] :as mutation-props}]
  (when-let [media-item (core.side-effects/get-item env item-id)]
            (letfn [(f [] (delete-item-f env mutation-props))]
                   (time/set-timeout! f media-browser.config/PERMANENT-DELETE-AFTER))
            (core.side-effects/update-path-directories! env           media-item -)
            (core.side-effects/detach-item!             env parent-id media-item)
            (clean-collections! item-id)
            (return             item-id)))

(defn delete-items-temporary-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-ids (strings in vector)
  ;   :parent-id (string)}
  ;
  ; @return (strings in vector)
  [env {:keys [item-ids parent-id]}]
  (letfn [(f [result item-id]
             (conj result (delete-item-temporary-f env {:item-id item-id :parent-id parent-id})))]
         (reduce f [] item-ids)))

(defmutation delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-id (string)}
             ;
             ; @return (string)
             [env {:keys [item-id]}]
             {::pathom.co/op-name 'storage.media-browser/delete-item!}
             (let [parent-id (item-browser/item-id->parent-id env :storage.media-browser item-id)]
                  (delete-item-temporary-f env {:item-id item-id :parent-id parent-id})))

(defmutation delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (strings in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'storage.media-browser/delete-items!}
             (let [parent-id (item-browser/item-id->parent-id env :storage.media-browser (first item-ids))]
                  (delete-items-temporary-f env {:item-ids item-ids :parent-id parent-id})))

;; -- Undo delete item(s) mutations -------------------------------------------
;; ----------------------------------------------------------------------------

(defn undo-delete-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:items (namespaced map)
  ;   :parent-id (string)}
  ;
  ; @return (namespaced map)
  [env {:keys [item parent-id] :as mutation-props}]
  (when-let [media-item (core.side-effects/get-item env (:media/id item))]
            (core.side-effects/update-path-directories! env           media-item +)
            (core.side-effects/attach-item!             env parent-id media-item)
            (return media-item)))

(defn undo-delete-items-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:items (namespaced maps in vector)
  ;   :parent-id (string)}
  ;
  ; @return (namespaced maps in vector)
  [env {:keys [items parent-id]}]
  (letfn [(f [result item]
             (conj result (undo-delete-item-f env {:item item :parent-id parent-id})))]
         (reduce f [] items)))

(defmutation undo-delete-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'storage.media-browser/undo-delete-item!}
             (let [parent-id (item-browser/item->parent-id env :storage.media-browser item)]
                  (undo-delete-item-f env {:item item :parent-id parent-id})))

(defmutation undo-delete-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:items (namespaced maps in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [items]}]
             {::pathom.co/op-name 'storage.media-browser/undo-delete-items!}
             (let [parent-id (item-browser/item->parent-id env :storage.media-browser (first items))]
                  (undo-delete-items-f env {:items items :parent-id parent-id})))

;; -- Duplicate item(s) mutations ---------------------------------------------
;; ----------------------------------------------------------------------------

(defn duplicated-directory-prototype
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-id (string)
  ;   :item-id (string)
  ;   :parent-id (string)}
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props} document]
  (letfn [(f2 [{:media/keys [id] :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                        (vector/->items % f2))]
         (as-> document % (common/duplicated-document-prototype request %)
                          (if (= destination-id parent-id) % (update % :media/path f1)))))

(defn duplicated-file-prototype
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-id (string)
  ;   :item-id (string)
  ;   :parent-id (string)}
  ; @param (namespaced map) document
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props} document]
  (letfn [(f3 [{:media/keys [id filename] :as %}] (assoc % :media/filename (core.helpers/file-id->filename id filename)))
          (f2 [{:media/keys [id]          :as %}] (if (= id parent-id) {:media/id destination-id} %))
          (f1 [%]                                 (vector/->items % f2))]
         (as-> document % (common/duplicated-document-prototype request %)
                          (if (= destination-id parent-id) % (update % :media/path f1))
                          (f3 %))))

(defn duplicate-file-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-id (string)
  ;   :item-id (string)
  ;   :parent-id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props}]
  (let [prepare-f #(media-browser.prototypes/duplicated-file-prototype env mutation-props %)]
       (when-let [copy-item (mongo-db/duplicate-document! "storage" item-id {:prepare-f prepare-f})]
                 (if (= destination-id parent-id)
                     (core.side-effects/attach-item! env destination-id copy-item))
                 (core.side-effects/update-path-directories! env copy-item +)
                 (if-let [source-item (mongo-db/get-document-by-id "storage" item-id)]
                         (let [source-filename (get source-item :media/filename)
                               copy-filename   (get copy-item   :media/filename)]
                              (core.side-effects/duplicate-file! source-filename copy-filename)
                              (return copy-item))))))

(defn duplicate-directory-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:destination-id (string)
  ;   :item-id (string)
  ;   :parent-id (string)}
  ;
  ; @return (namespaced map)
  [{:keys [request] :as env} {:keys [destination-id item-id parent-id] :as mutation-props}]
  ; - A {:destination-id "..."} tulajdonságként átadott azonosító, annak a mappának az azonosítója,
  ;   ahol a teljes duplikálási folyamat történik (kiindulási pont).
  ;
  ; - Az {:item-id "..."} tulajdonságként átadott azonosító, annak a mappának az azonosítója,
  ;   ami éppen duplikálódik.
  ;
  ; - A {:parent-id "..."} tulajdonságként átadott azonosító, annak a mappának az azonosítója,
  ;   ahol az éppen duplikálódó mappa található.
  ;
  ; A) Ha az éppen duplikálódó mappa tartalmaz gyermek-elemeket, ...
  ;    ... a copy-item térképpel az utolsó gyermek-elem sikeres duplikálása után tér vissza.
  ;
  ; B) Ha az éppen duplikálódó mappa NEM tartalmaz további elemeket, ...
  ;    ... visszatér a copy-item térképpel.
  (let [prepare-f #(media-browser.prototypes/duplicated-directory-prototype env mutation-props %)]
       (when-let [copy-item (mongo-db/duplicate-document! "storage" item-id {:prepare-f prepare-f})]
                 (when (= destination-id parent-id)
                       (core.side-effects/attach-item!             env destination-id copy-item)
                       (core.side-effects/update-path-directories! env                copy-item))
                 (let [items (get copy-item :media/items)]
                      (if (vector/nonempty? items)
                          ; A)
                          (doseq [{:media/keys [id]} items]
                                 (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env id)]
                                         (let [destination-id (:id copy-item)
                                               mutation-props {:destination-id destination-id :item-id id :parent-id item-id}]
                                              (case mime-type "storage/directory" (duplicate-directory-f env mutation-props)
                                                                                  (duplicate-file-f      env mutation-props))
                                              (return copy-item))))
                          ; B)
                          (return copy-item))))))

(defn duplicate-item-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-id (string)
  ;   :parent-id (string)}
  ;
  ; @return (namespaced map)
  [env {:keys [item-id parent-id]}]
  (if-let [{:media/keys [mime-type]} (core.side-effects/get-item env item-id)]
          (case mime-type "storage/directory" (duplicate-directory-f env {:item-id item-id :parent-id parent-id :destination-id parent-id})
                                              (duplicate-file-f      env {:item-id item-id :parent-id parent-id :destination-id parent-id}))))

(defn duplicate-items-f
  ; @param (map) env
  ; @param (map) mutation-props
  ;  {:item-ids (strings in vector)
  ;   :parent-id (string)}
  ;
  ; @return (namespaced maps in vector)
  [env {:keys [item-ids parent-id]}]
  ; A duplicate-items-f függvény végigiterál az item-ids vektor elemeiként átadott azonosítókon
  ; és alkalmazza rajtuk a duplicate-item-f függvényt, majd az egyes visszatérési értékeket
  ; a visszatérési érték vektorban felsorolja.
  (letfn [(f [result item-id]
             (conj result (duplicate-item-f env {:item-id item-id :parent-id parent-id})))]
         (reduce f [] item-ids)))

(defmutation duplicate-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env {:keys [item]}]
             {::pathom.co/op-name 'storage.media-browser/duplicate-item!}
             (let [item-id   (get item :media/id)
                   parent-id (item-browser/item-id->parent-id env :storage.media-browser item-id)]
                  (duplicate-item-f env {:item-id item-id :parent-id parent-id})))

(defmutation duplicate-items!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item-ids (strings in vector)}
             ;
             ; @return (namespaced maps in vector)
             [env {:keys [item-ids]}]
             {::pathom.co/op-name 'storage.media-browser/duplicate-items!}
             (let [parent-id (item-browser/item-id->parent-id env :storage.media-browser (first item-ids))]
                  (duplicate-items-f env {:item-ids item-ids :parent-id parent-id})))

;; -- Update item(s) mutations ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-item-f
  ; @param (map) env
  ;  {:request (map)}
  ; @param (map) mutation-props
  ;  {:item (namespaced map)}
  ;
  ; @return (namespaced map)
  [{:keys [request]} {:keys [item]}]
  (mongo-db/save-document! "storage" item {:prepare-f #(common/updated-document-prototype request %)}))

(defmutation update-item!
             ; @param (map) env
             ; @param (map) mutation-props
             ;  {:item (namespaced map)}
             ;
             ; @return (namespaced map)
             [env mutation-props]
             {::pathom.co/op-name 'storage.media-browser/update-item!}
             (update-item-f env mutation-props))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (functions in vector)
(def HANDLERS [delete-item! delete-items! duplicate-item! duplicate-items! undo-delete-item!
               undo-delete-items! update-item!])

(pathom/reg-handlers! ::handlers HANDLERS)
