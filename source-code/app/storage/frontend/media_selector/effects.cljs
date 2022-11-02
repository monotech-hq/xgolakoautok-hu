
(ns app.storage.frontend.media-selector.effects
    (:require [app.storage.frontend.media-selector.helpers :as media-selector.helpers]
              [app.storage.frontend.media-selector.subs    :as media-selector.subs]
              [app.storage.frontend.media-selector.views   :as media-selector.views]
              [engines.item-browser.api                    :as item-browser]
              [re-frame.api                                :as r :refer [r]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-selector/render-selector!
  ; @param (keyword) selector-id
  (fn [_ [_ selector-id]]
      [:ui/render-popup! :storage.media-selector/view
                         {:content [media-selector.views/view selector-id]}]))

(r/reg-event-fx :storage.media-selector/load-selector!
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {:autosave? (boolean)(opt)
  ;    Default: false
  ;   :extensions (strings in vector)(opt)
  ;   :max-count (integer)(opt)
  ;    Default: 8
  ;   :multi-select? (boolean)(opt)
  ;    Default: false
  ;   :on-save (metamorphic-event)(opt)
  ;    Az esemény utolsó paraméterként megkapja a kiválasztott elem(ek)et.
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [:storage.media-selector/load-selector! {...}]
  ;
  ; @usage
  ;  [:storage.media-selector/load-selector! :my-selector {...}]
  [r/event-vector<-id]
  (fn [_ [_ _ {:keys [autosave? extensions multi-select? on-save value-path]}]]
      {:dispatch-n [[:item-selector/load-selector! :storage.media-selector
                                                   {:autosave?     autosave?
                                                    :extensions    extensions
                                                    :export-item-f media-selector.helpers/export-item-f
                                                    :import-id-f   media-selector.helpers/import-id-f
                                                    :multi-select? multi-select?
                                                    :on-save       [:storage.media-selector/selection-saved on-save]
                                                    :value-path    value-path}]
                    [:storage.media-selector/render-selector!]]}))

(r/reg-event-fx :storage.media-selector/selection-saved
  ; @param (metamorphic-event) on-save
  ; @param (string or strings in vector) exported-items
  (fn [_ [_ on-save exported-items]]
      (let [on-save (r/metamorphic-event<-params on-save exported-items)]
           {:dispatch-n [on-save [:ui/remove-popup! :storage.media-selector/view]]})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-selector/create-directory!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-selector)]
           [:storage.directory-creator/load-creator! {:browser-id :storage.media-selector :destination-id destination-id}])))

(r/reg-event-fx :storage.media-selector/upload-files!
  (fn [{:keys [db]} [_ selected-option]]
      (let [destination-id (r item-browser/get-current-item-id db :storage.media-selector)]
           [:storage.file-uploader/load-uploader! {:browser-id :storage.media-selector :destination-id destination-id}])))
