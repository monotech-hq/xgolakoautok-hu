
(ns app.storage.frontend.alias-editor.views
    (:require [elements.api        :as elements]
              [io.api              :as io]
              [layouts.popup-a.api :as popup-a]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn cancel-button
  [_]
  [elements/button ::cancel-button
                   {:font-size   :xs
                    :hover-color :highlight
                    :indent      {:all :xxs}
                    :keypress    {:key-code 27 :required? true}
                    :on-click    [:x.ui/remove-popup! :storage.alias-editor/view]
                    :preset      :cancel}])

(defn header-label
  [_]
  [elements/label ::header-label
                  {:content     :rename!
                   :line-height :block}])

(defn rename-button
  [media-item]
  [elements/button ::rename-button
                   {:color       :primary
                    :font-size   :xs
                    :hover-color :highlight
                    :keypress    {:key-code 13 :required? true}
                    :indent      {:all :xxs}
                    :label       :rename!
                    :on-click    [:storage.alias-editor/update-item-alias! media-item]}])

(defn header
  [media-item]
  [elements/horizontal-polarity ::header
                                {:start-content  [cancel-button media-item]
                                 :middle-content [header-label  media-item]
                                 :end-content    [rename-button media-item]}])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn alias-field
  [{:keys [alias]}]
  [elements/text-field ::alias-field
                       {:autoclear?    true
                        :autofocus?    true
                        :initial-value alias
                        :indent        {:bottom :xs :top :m :vertical :s}
                        :validator     {:f               io/filename-valid?
                                        :invalid-message :invalid-name
                                        :pre-validate?   true}
                        :placeholder   :name
                        :value-path    [:storage :alias-editor/item-alias]}])

(defn body
  [media-item]
  [alias-field media-item])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  [media-item]
  [popup-a/layout :storage.alias-editor/view
                  {:body      [body   media-item]
                   :header    [header media-item]
                   :min-width :s}])
