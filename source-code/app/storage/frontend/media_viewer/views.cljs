
(ns app.storage.frontend.media-viewer.views
    (:require [elements.api        :as elements]
              [io.api              :as io]
              [layouts.popup-b.api :as popup-b]
              [mid-fruits.css      :as css]
              [re-frame.api        :as r]
              [x.app-media.api     :as x.media]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn close-icon-button
  [viewer-id]
  [:div {:style {:position :fixed :right 0 :top :0}}
        [elements/icon-button ::close-icon-button
                              {:color    :invert
                               :keypress {:key-code 27}
                               :on-click [:ui/remove-popup! :storage.media-viewer/view]
                               :preset   :close}]])

;; -- PDF-item components -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn pdf-item-pdf
  [viewer-id]
  (let [% @(r/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       [:iframe.storage--media-viewer--pdf {:src   (-> % :item-filename x.media/filename->media-storage-uri)
                                            :style {:border-radius (css/var  "border-radius-m")
                                                    :height        (css/calc "100vh - 96px")
                                                    :width         (css/calc "100vw - 96px")}}]))

(defn pdf-item
  [viewer-id]
  [:div.storage--media-viewer--pdf-item [pdf-item-pdf viewer-id]])

;; -- Image-item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-item-icon
  [_]
  [:div.storage--media-viewer--icon {:style {:align-items "center" :display "flex" :justify-content "center"
                                             :height "100%" :left "0" :position "absolute" :top "0" :width "100%"}}
                                    [elements/icon {:icon :insert_drive_file :color :invert}]])

(defn image-item-image
  [viewer-id]
  (let [% @(r/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       [:img.storage--media-viewer--image {:src   (-> % :item-filename x.media/filename->media-storage-uri)
                                           :style {:border-radius (css/var  "border-radius-m")
                                                   :max-height    (css/calc "100vh - 96px")
                                                   :max-width     (css/calc "100vw - 96px")}}]))

(defn image-item
  [viewer-id]
  [:div.storage--media-viewer--image-item {:style {:height "100%" :width "100%"}}
                                         [image-item-icon  viewer-id]
                                         [image-item-image viewer-id]])

;; -- Media-item components ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn media-item
  [viewer-id]
  (let [% @(r/subscribe [:storage.media-viewer/get-current-item-props viewer-id])]
       (case (-> % :item-filename io/filename->mime-type)
             "application/pdf" [pdf-item   viewer-id]
                               [image-item viewer-id])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view-structure
  [viewer-id]
  [:<> [media-item        viewer-id]
       [close-icon-button viewer-id]])

(defn view
  [viewer-id]
  [popup-b/layout :storage.media-viewer/view
                  {:content [view-structure viewer-id]}])
