package com.example.astrobin.api

import com.squareup.moshi.Json

data class AstroImage(
  @field:Json(name="id") val id: Int,
  @field:Json(name="hash") val hash: String?,
  @field:Json(name="title") val title: String?,
  @field:Json(name="user") val user: String, // username

  // dates
  @field:Json(name="published") val published: String,
  @field:Json(name="updated") val updated: String,
  @field:Json(name="uploaded") val uploaded: String,

  // astrometry
  @field:Json(name="is_solved") val is_solved: Boolean,
  @field:Json(name="solution_status") val solution_status: String,
  @field:Json(name="ra") val ra: String?, // float
  @field:Json(name="dec") val dec: String?, // Float
  @field:Json(name="pixscale") val pixscale: String?, // Float
  @field:Json(name="radius") val radius: String?, // float
  @field:Json(name="orientation") val orientation: String?, // Float
  @field:Json(name="w") val w: Int,
  @field:Json(name="h") val h: Int,

  // images
  @field:Json(name="url_advanced_solution") val url_advanced_solution: String?,
  @field:Json(name="url_duckduckgo") val url_duckduckgo: String,
  @field:Json(name="url_duckduckgo_small") val url_duckduckgo_small: String,
  @field:Json(name="url_gallery") val url_gallery: String,
  @field:Json(name="url_hd") val url_hd: String,
  @field:Json(name="url_histogram") val url_histogram: String,
  @field:Json(name="url_real") val url_real: String,
  @field:Json(name="url_regular") val url_regular: String,
  @field:Json(name="url_skyplot") val url_skyplot: String?,
  @field:Json(name="url_solution") val url_solution: String?,
  @field:Json(name="url_thumb") val url_thumb: String,

  // statistics
  @field:Json(name="comments") val comments: Int,
  @field:Json(name="likes") val likes: Int,
  @field:Json(name="views") val views: Int,

  // technical card
  @field:Json(name="imaging_cameras") val imaging_cameras: List<String>,
  @field:Json(name="imaging_telescopes") val imaging_telescopes: List<String>,
  @field:Json(name="data_source") val data_source: String,
  @field:Json(name="locations") val locations: List<String>,
  @field:Json(name="remote_source") val remote_source: String?,
  @field:Json(name="subjects") val subjects: List<String>,

  // other
  @field:Json(name="animated") val animated: Boolean,
  @field:Json(name="bookmarks") val bookmarks: Int,
  @field:Json(name="is_final") val is_final: Boolean,
  @field:Json(name="license") val license: Int,
  @field:Json(name="license_name") val license_name: String,
  @field:Json(name="link") val link: String?,
  @field:Json(name="link_to_fits") val link_to_fits: String?,
  @field:Json(name="resource_uri") val resource_uri: String,
  @field:Json(name="revisions") val revisions: List<String>,
) {
  val aspectRatio: Float get() = w.toFloat() / h.toFloat()
}

data class AstroImageV2(
  @field:Json(name="pk") val pk: Int,
  @field:Json(name="user") val user: Int,
  @field:Json(name="hash") val hash: String,
  @field:Json(name="title") val title: String,
  @field:Json(name="imageFile") val imageFile: String?,
  @field:Json(name="isWip") val isWip: Boolean,
  @field:Json(name="skipNotifications") val skipNotifications: Boolean,
  @field:Json(name="w") val w: Int,
  @field:Json(name="h") val h: Int,
  @field:Json(name="imagingTelescopes") val imagingTelescopes: List<AstroProduct>,
  @field:Json(name="imagingCameras") val imagingCameras: List<AstroProduct>,
  @field:Json(name="guidingTelescopes") val guidingTelescopes: List<AstroProduct>,
  @field:Json(name="guidingCameras") val guidingCameras: List<AstroProduct>,
  @field:Json(name="focalReducers") val focalReducers: List<AstroProduct>,
  @field:Json(name="mounts") val mounts: List<AstroProduct>,
  @field:Json(name="accessories") val accessories: List<AstroProduct>,
  @field:Json(name="software") val software: List<AstroProduct>,
//  @field:Json(name="imagingTelescopes2") val imagingTelescopes2: List<TelescopeInterface2>,
//  @field:Json(name="imagingCameras2") val imagingCameras2: List<CameraInterface2>,
//  @field:Json(name="guidingTelescopes2") val guidingTelescopes2: List<TelescopeInterface2>,
//  @field:Json(name="guidingCameras2") val guidingCameras2: List<CameraInterface2>,
//  @field:Json(name="mounts2") val mounts2: List<MountInterface2>,
//  @field:Json(name="filters2") val filters2: List<FilterInterface2>,
//  @field:Json(name="accessories2") val accessories2: List<AccessoryInterface2>,
//  @field:Json(name="software2") val software2: List<SoftwareInterface2>,
  @field:Json(name="published") val published: String,
  @field:Json(name="license") val license: String,
  @field:Json(name="description") val description: String?,
  @field:Json(name="descriptionBbcode") val descriptionBbcode: String?,
  @field:Json(name="link") val link: String?,
  @field:Json(name="linkToFits") val linkToFits: String?,
  @field:Json(name="acquisitionType") val acquisitionType: String,
  @field:Json(name="subjectType") val subjectType: String,
  @field:Json(name="solarSystemMainSubject") val solarSystemMainSubject: String?,
  @field:Json(name="dataSource") val dataSource: String,
  @field:Json(name="remoteSource") val remoteSource: String?,
  @field:Json(name="partOfGroupSet") val partOfGroupSet: List<Int>,
  @field:Json(name="mouseHoverImage") val mouseHoverImage: String,
  @field:Json(name="allowComments") val allowComments: Boolean,
  @field:Json(name="squareCropping") val squareCropping: String,
  @field:Json(name="watermark") val watermark: Boolean,
  @field:Json(name="watermarkText") val watermarkText: String?,
  @field:Json(name="watermarkPosition") val watermarkPosition: String,
  @field:Json(name="watermarkSize") val watermarkSize: String,
  @field:Json(name="watermarkOpacity") val watermarkOpacity: Float,
  @field:Json(name="sharpenThumbnails") val sharpenThumbnails: Boolean,
  @field:Json(name="keyValueTags") val keyValueTags: String,
  @field:Json(name="locations") val locations: List<Int>,
  // TODO: we should make sure to honor this if it isn't enforced by backend
  @field:Json(name="fullSizeDisplayLimitation") val fullSizeDisplayLimitation: String,
  // TODO: we should make sure to honor this if it isn't enforced by backend
  @field:Json(name="downloadLimitation") val downloadLimitation: String,
  @field:Json(name="thumbnails") val thumbnails: List<AstroThumbnail>,

  // Ephemeral form fields
//  showGuidingEquipment?: boolean;
) {
  private fun urlFor(alias: String) = thumbnails.single { it.alias == alias }.url
  val url_story: String get() = urlFor("story")
  val url_regular: String get() = urlFor("regular")
  val url_hd: String get() = urlFor("hd")
  val url_qhd: String get() = urlFor("qhd")

  val url_histogram: String get() = imageUrl(hash,"histogram")

  val aspectRatio: Float get() = w.toFloat() / h.toFloat()

  val bookmarksCount: Int
    get() = 6 // TODO: ask about including this in the API
  val likesCount: Int
    get() = 126 // TODO: ask about including this in the API
}

data class AstroImageRevision(
  val id: Int,
)

data class TopPick(
  @field:Json(name="date") val date: String,
  @field:Json(name="image") val image: String,
  @field:Json(name="resource_uri") val resource_uri: String
) {
  val hash: String get() = image.substringAfterLast('/')
  val url_regular: String get() = imageUrl(hash, "regular")
  val url_thumb: String get() = imageUrl(hash, "thumb")
  val url_real: String get() = imageUrl(hash, "real")
  val url_gallery: String get() = imageUrl(hash, "gallery")
  val url_hd: String get() = imageUrl(hash, "hd")
}

data class AstroProduct(
  @field:Json(name="pk") val pk: Int,
  @field:Json(name="make") val make: String?,
  @field:Json(name="name") val name: String,
)

data class ThumbnailGroup(
  @field:Json(name="image") val image: Int,
  @field:Json(name="pk") val pk: Int,
  @field:Json(name="revision") val revision: String,
  @field:Json(name="real") val real: String,
  @field:Json(name="hd") val hd: String,
  @field:Json(name="regular") val regular: String,
  @field:Json(name="gallery") val gallery: String,
  @field:Json(name="thumb") val thumb: String,
)

data class AstroThumbnail(
  @field:Json(name="id") val id: Int,
  @field:Json(name="revision") val revision: String,
  @field:Json(name="alias") val alias: String,
  @field:Json(name="url") val url: String,
)

data class PlateSolve(
  @field:Json(name="id") val id: Int,
  @field:Json(name="status") val status: Int,
  @field:Json(name="submission_id") val submission_id: Int,
  @field:Json(name="object_id") val object_id: String,
  @field:Json(name="image_file") val image_file: String,
  @field:Json(name="skyplot_zoom1") val skyplot_zoom1: String,
  @field:Json(name="objects_in_field") val objects_in_field: String,
  @field:Json(name="ra") val ra: String,
  @field:Json(name="dec") val dec: String,
  @field:Json(name="pixscale") val pixscale: String,
  @field:Json(name="orientation") val orientation: String,
  @field:Json(name="radius") val radius: String,
//  @field:Json(name="annotations") val annotations: String,
//  @field:Json(name="pixinsight_serial_number") val pixinsight_serial_number: String?,
//  @field:Json(name="pixinsight_svg_annotation_hd") val pixinsight_svg_annotation_hd: String?,
//  @field:Json(name="pixinsight_svg_annotation_regular") val pixinsight_svg_annotation_regular: String,
//  @field:Json(name="advanced_ra") val advanced_ra: String?,
//  @field:Json(name="advanced_ra_top_left") val advanced_ra_top_left: String?,
//  @field:Json(name="advanced_ra_top_right") val advanced_ra_top_right: String?,
//  @field:Json(name="advanced_ra_bottom_left") val advanced_ra_bottom_left: String?,
//  @field:Json(name="advanced_ra_bottom_right") val advanced_ra_bottom_right: String?,
//  @field:Json(name="advanced_dec") val advanced_dec: String?,
//  @field:Json(name="advanced_dec_top_left") val advanced_dec_top_left: String?,
//  @field:Json(name="advanced_dec_top_right") val advanced_dec_top_right: String?,
//  @field:Json(name="advanced_dec_bottom_left") val advanced_dec_bottom_left: String?,
//  @field:Json(name="advanced_dec_bottom_right") val advanced_dec_bottom_right: String?,
//  @field:Json(name="advanced_pixscale") val advanced_pixscale: String?,
//  @field:Json(name="advanced_orientation") val advanced_orientation: String?,
//  @field:Json(name="advanced_flipped") val advanced_flipped: Boolean?,
//  @field:Json(name="advanced_wcs_transformation") val advanced_wcs_transformation: String?,
//  @field:Json(name="advanced_matrix_rect") val advanced_matrix_rect: String?,
//  @field:Json(name="advanced_matrix_delta") val advanced_matrix_delta: String?,
//  @field:Json(name="advanced_ra_matrix") val advanced_ra_matrix: String?,
//  @field:Json(name="advanced_dec_matrix") val advanced_dec_matrix: String?,
//  @field:Json(name="advanced_annotations") val advanced_annotations: String?,
//  @field:Json(name="advanced_annotations_regular") val advanced_annotations_regular: String?,
  @field:Json(name="settings") val settings: Int,
  @field:Json(name="content_type") val content_type: Int,
  @field:Json(name="advanced_settings") val advanced_settings: Int?,
)

private fun imageUrl(hash: String, type: String) = "https://www.astrobin.com/$hash/0/rawthumb/$type/"
