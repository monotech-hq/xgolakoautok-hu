
(ns app.website-post.frontend.editor.effects
  (:require [app.website-post.frontend.editor.views :as editor.views]
            [re-frame.api                           :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :website-post.editor/load-editor!
  {:dispatch-n [[:x.gestures/init-view-handler! :website-post.editor
                                                {:default-view-id :basic-data}]
                [:website-post.editor/render-editor!]]})

(r/reg-event-fx :website-post.editor/render-editor!
  [:x.ui/render-surface! :website-post.editor/view
                         {:content #'editor.views/view}])
