
(ns app.storage.frontend.media-browser.target-selector
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-cancel-button
  [_]
  [elements/button :header-cancel-button
                   {;:preset :cancel-button :indent _ :keypress {:key-code 27}
                    :on-click [:x.ui/remove-popup! :storage.media-picker/view]}])

(defn header-label
  [_]
  (let [header-label @(r/subscribe [:item-browser/get-item-label :storage.media-browser])]
       [elements/label ::header-label
                       {:content     header-label
                        :line-height :block}]))

(defn header-select-button
  [_]
  (let [no-items-selected? @(r/subscribe [:storage.media-picker/no-items-selected?])]
       [elements/button :header-select-button
                        {:disabled? no-items-selected?
                         ;:preset :select-button :indent _ :keypress {:key-code 13}
                         :on-click [:storage.media-picker/save-selected-items!]}]))

(defn header-label-bar
  [selector-id]
  [elements/horizontal-polarity ::header-label-bar
                                {:start-content  [header-cancel-button selector-id]
                                 :middle-content [header-label         selector-id]
                                 :end-content    [header-select-button selector-id]}])

(defn header
  []
  [header-label-bar :x])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  []
  [:div "target-selector"])

;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :storage.media-browser/render-target-selector!
  [:x.ui/render-popup! :storage.media-browser/target-selector
                       {:body   #'body
                        :header #'header}])
