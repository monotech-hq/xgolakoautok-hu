
(ns app.price-quotes.frontend.preview.views
    (:require [app.common.frontend.api                 :as common]
              [app.price-quotes.frontend.handler.state :as handler.state]
              [elements.api                            :as elements]
              [layouts.popup-b.api                     :as popup-b]
              [mid-fruits.css                          :as css]
              [re-frame.api                            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn close-icon-button
  [viewer-id]
  [:div {:style {:position :fixed :right 0 :top :0}}
        [elements/icon-button ::close-icon-button
                              {:color    :invert
                               :keypress {:key-code 27}
                               :on-click [:ui/remove-popup! :price-quotes.preview/view]
                               :preset   :close}]])

(defn- preview-download-error-label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/label ::preview-download-error-label
                       {:color            :warning
                        :content          :download-error
                        :disabled?        editor-disabled?
                        :horizontal-align :center
                        :selectable?      true}]))

(defn- preview-downloading-label
  []
  (let [editor-disabled? @(r/subscribe [:item-editor/editor-disabled? :price-quotes.editor])]
       [elements/label ::preview-downloading-label
                       {:color            :invert
                        :content          :downloading...
                        :disabled?        editor-disabled?
                        :horizontal-align :center}]))

(defn- preview-pdf
  []
  [common/pdf-preview ::price-quote-preview-pdf {:src @handler.state/PDF-OBJECT-URL
                                                 :height (css/calc "100vh - 96px")
                                                 :width  (css/calc "100vw - 96px")}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  []
  [:<> [close-icon-button]
       (case @handler.state/PDF-DOWNLOAD-STATUS :downloaded     [preview-pdf]
                                                :downloading    [preview-downloading-label]
                                                :download-error [preview-download-error-label]
                                                                [:<>])])

(defn view
  [popup-id]
  [popup-b/layout popup-id
                  {:content #'view-structure}])
