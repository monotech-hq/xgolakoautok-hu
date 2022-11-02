
(ns app.common.frontend.item-selector.subs
    (:require [engines.item-lister.api :as item-lister]
              [mid-fruits.candy        :refer [return]]
              [mid-fruits.vector       :as vector]
              [re-frame.api            :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-item-count
  ; @param (keyword) selector-id
  ; @param (string) item-id
  ;
  ; @return (integer)
  [db [_ selector-id item-id]]
  ; TODO#5060
  ;
  ; BUG#8001
  ; Ha az elem nem volt kiválasztva a kijelölések importálásakor (nem kapta meg a kezdeti darabszámot),
  ; és nem volt még módosítva az elem darabszáma, akkor a függvény az alapértelmezett darabszámmal tér vissza.
  (if-let [item-selected? (r item-lister/item-selected? db selector-id item-id)]
          (get-in db [:engines :engine-handler/meta-items selector-id :item-counts item-id] 1)
          (return 0)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-stored-selection
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:value-path (vector)}
  ;
  ; @return (vector)
  [db [_ selector-id]]
  (let [value-path       (r item-lister/get-meta-item db selector-id :value-path)
        stored-selection (get-in db value-path)]
       (cond (vector? stored-selection) (return  stored-selection)
             (some?   stored-selection) [stored-selection]
             :return [])))

(defn import-selection
  ; @param (keyword) selector-id
  ;
  ; @return (?)
  [db [_ selector-id]]
  (let [import-id-f      (r item-lister/get-meta-item db selector-id :import-id-f)
        stored-selection (r get-stored-selection      db selector-id)]
       (vector/->items stored-selection import-id-f)))

(defn import-item-counts
  ; @param (keyword) selector-id
  ;
  ; @return (map)
  [db [_ selector-id]]
  (let [import-count-f   (r item-lister/get-meta-item db selector-id :import-count-f)
        import-id-f      (r item-lister/get-meta-item db selector-id :import-id-f)
        stored-selection (r get-stored-selection db selector-id)]
       (letfn [(f [result n] (assoc result (import-id-f    n)
                                           (import-count-f n)))]
              (reduce f {} stored-selection))))

(defn export-selection
  ; @param (keyword) selector-id
  ;
  ; @return (* or vector)
  [db [_ selector-id]]
  (let [exported-selection (r item-lister/get-meta-item db selector-id :exported-selection)]
       (if-let [multi-select? (r item-lister/get-meta-item db selector-id :multi-select?)]
               (return exported-selection)
               (first  exported-selection))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn selection-changed?
  ; @param (keyword) selector-id
  ;
  ; @return (boolean)
  [db [_ selector-id]]
  (not= (r get-stored-selection      db selector-id)
        (r item-lister/get-meta-item db selector-id :exported-selection)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-on-change
  ; @param (keyword) selector-id
  ;
  ; @return (metamorphic-event)
  [db [_ selector-id]]
  (if-let [on-change (r item-lister/get-meta-item db selector-id :on-change)]
          (if-let [selection-changed? (r selection-changed? db selector-id)]
                  (let [exported-selection (r export-selection db selector-id)]
                       (r/metamorphic-event<-params on-change exported-selection)))))

(defn get-on-save
  ; @param (keyword) selector-id
  ;
  ; @return (metamorphic-event)
  [db [_ selector-id]]
  (if-let [on-save (r item-lister/get-meta-item db selector-id :on-save)]
          (let [exported-selection (r export-selection db selector-id)]
               (r/metamorphic-event<-params on-save exported-selection))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn autosaving?
  ; @param (keyword) selector-id
  ;
  ; @return (boolean)
  [db [_ selector-id]]
  (r item-lister/get-meta-item db selector-id :autosave-id))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:item-selector/get-item-count :my-selector "my-item"]
(r/reg-sub :item-selector/get-item-count get-item-count)

; @usage
;  [:item-selector/autosaving? :my-selector]
(r/reg-sub :item-selector/autosaving? autosaving?)
