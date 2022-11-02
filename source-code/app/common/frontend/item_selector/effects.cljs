
(ns app.common.frontend.item-selector.effects
    (:require [app.common.frontend.item-selector.events     :as item-selector.events]
              [app.common.frontend.item-selector.prototypes :as item-selector.prototypes]
              [app.common.frontend.item-selector.subs       :as item-selector.subs]
              [engines.item-lister.api                      :as item-lister]
              [mid-fruits.random                            :as random]
              [re-frame.api                                 :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-selector/load-selector!
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :export-item-f (function)(opt)
  ;    Default: (fn [item-id item item-count] item-id)
  ;   :import-count-f (function)(opt)
  ;    Default: (fn [_] 1)
  ;   :import-id-f (function)(opt)
  ;    Default: (fn [item-id] item-id)
  ;   :multi-select? (boolean)(opt)
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [:item-selector/load-selector! :my-selector {...}]
  (fn [{:keys [db]} [_ selector-id selector-props]]
      (let [selector-props (item-selector.prototypes/selector-props-prototype selector-props)]
           {:db (r item-selector.events/load-selector! db selector-id selector-props)})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :item-selector/save-selection!
  ; @param (keyword) selector-id
  ; @param (keyword)(opt) autosave-id
  (fn [{:keys [db]} [_ selector-id autosave-id]]
      (if (or (nil? autosave-id)
              (=    autosave-id (r item-lister/get-meta-item db selector-id :autosave-id)))
          {:db          (r item-selector.events/store-exported-selection! db selector-id)
           :dispatch-n [(r item-selector.subs/get-on-save                 db selector-id)
                        (r item-selector.subs/get-on-change               db selector-id)]})))

(r/reg-event-fx :item-selector/abort-autosave!
  ; @param (keyword) selector-id
  (fn [{:keys [db]} [_ selector-id]]
      {:db (r item-selector.events/abort-autosave! db selector-id)}))

(r/reg-event-fx :item-selector/autosave-selection!
  ; @param (keyword) selector-id
  (fn [{:keys [db]} [_ selector-id]]
      (let [autosave-id (random/generate-keyword)]
           {:db             (r item-lister/set-meta-item! db selector-id :autosave-id autosave-id)
            :dispatch-later [{:ms 1500 :dispatch [:item-selector/save-selection! selector-id autosave-id]}]})))

(r/reg-event-fx :item-selector/item-clicked
  ; @param (keyword) selector-id
  ; @param (string) item-id
  (fn [{:keys [db]} [_ selector-id item-id]]
      (let [db (r item-selector.events/toggle-item-selection! db selector-id item-id)]
           (if-let [autosave? (r item-lister/get-meta-item db selector-id :autosave?)]
                   ; Ha az item-selector {:autosave? true} beállítással van használva ...
                   ; ... az esetlegesen már folyamatban lévő automatikus mentést leállítja.
                   (if-let [item-selected? (r item-lister/item-selected? db selector-id item-id)]
                           {:dispatch [:item-selector/autosave-selection! selector-id]
                            :db       (r item-selector.events/abort-autosave! db selector-id)}
                           {:db       (r item-selector.events/abort-autosave! db selector-id)})
                   ; Ha az item-selector {:autosave? false} beállítással van használva ...
                   {:db db}))))
