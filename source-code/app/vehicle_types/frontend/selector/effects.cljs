
(ns app.vehicle-types.frontend.selector.effects
    (:require [app.vehicle-types.frontend.selector.helpers :as selector.helpers]
              [app.vehicle-types.frontend.selector.views   :as selector.views]
              [re-frame.api                                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :vehicle-types.selector/render-selector!
  ; @param (keyword) selector-id
  [:x.ui/render-popup! :vehicle-types.selector/view
                       {:content #'selector.views/view}])

(r/reg-event-fx :vehicle-types.selector/load-selector!
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :model-id (string)
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [:vehicle-types.selector/load-selector! {...}]
  ;
  ; @usage
  ;  [:vehicle-types.selector/load-selector! :my-selector {...}]
  [r/event-vector<-id]
  (fn [_ [_ _ {:keys [autosave? model-id multi-select? on-change on-save value-path]}]]
      {:dispatch-n [[:item-selector/load-selector! :vehicle-types.selector
                                                   {:autosave?     autosave?
                                                    :export-item-f selector.helpers/export-item-f
                                                    :import-id-f   selector.helpers/import-id-f
                                                    :model-id      model-id
                                                    :multi-select? multi-select?
                                                    :on-change     on-change
                                                    :on-save       [:vehicle-types.selector/selection-saved on-save]
                                                    :value-path    value-path}]
                    [:vehicle-types.selector/render-selector!]]}))

(r/reg-event-fx :vehicle-types.selector/selection-saved
  ; @param (metamorphic-event) on-save
  ; @param (maps in vector) exported-items
  (fn [_ [_ on-save exported-items]]
      (let [on-save (r/metamorphic-event<-params on-save exported-items)]
           {:dispatch-n [on-save [:x.ui/remove-popup! :vehicle-types.selector/view]]})))
