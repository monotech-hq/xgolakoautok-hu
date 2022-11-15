
(ns app.clients.frontend.selector.effects
    (:require [app.clients.frontend.selector.helpers :as selector.helpers]
              [app.clients.frontend.selector.views   :as selector.views]
              [re-frame.api                          :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :clients.selector/render-selector!
  ; @param (keyword) selector-id
  [:x.ui/render-popup! :clients.selector/view
                       {:content #'selector.views/view}])

(r/reg-event-fx :clients.selector/load-selector!
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-change (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [:clients.selector/load-selector! {...}]
  ;
  ; @usage
  ;  [:clients.selector/load-selector! :my-selector {...}]
  [r/event-vector<-id]
  (fn [_ [_ _ {:keys [autosave? multi-select? on-change on-save value-path]}]]
      {:dispatch-n [[:item-selector/load-selector! :clients.selector
                                                   {:autosave?     autosave?
                                                    :export-item-f selector.helpers/export-item-f
                                                    :import-id-f   selector.helpers/import-id-f
                                                    :multi-select? multi-select?
                                                    :on-change     on-change
                                                    :on-save       [:clients.selector/selection-saved on-save]
                                                    :value-path    value-path}]
                    [:clients.selector/render-selector!]]}))

(r/reg-event-fx :clients.selector/selection-saved
  ; @param (metamorphic-event) on-save
  ; @param (map) exported-item
  (fn [_ [_ on-save exported-item]]
      (let [on-save (r/metamorphic-event<-params on-save exported-item)]
           {:dispatch-n [on-save [:x.ui/remove-popup! :clients.selector/view]]})))
